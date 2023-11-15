package com.example.filmus.domain.movieAPI


import android.annotation.SuppressLint
import android.util.Base64
import android.util.Log
import com.example.filmus.common.Constants
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import java.security.cert.X509Certificate
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


// за основу взято https://github.com/lrdcxdes/LFilms/blob/master/app/src/main/java/dev/lrdcxdes/lfilms/api/Api.kt
class Api {
    private var scheme = Constants.WATCH_SCHEME
    private var host = Constants.WATCH_API_URL


    @SuppressLint("CustomX509TrustManager", "TrustAllX509TrustManager")
    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    })

    private var sslContext: SSLContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }


    private var client: OkHttpClient = OkHttpClient.Builder().callTimeout(15, TimeUnit.SECONDS)
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }.build()


    private fun getScheme(): String {
        return scheme
    }

    private fun getHost(): String {
        return host
    }

    suspend fun searchAjax(query: String): String? = suspendCoroutine { continuation ->
        val url = HttpUrl.Builder().scheme(getScheme()).host(getHost()).addPathSegment("engine")
            .addPathSegment("ajax").addPathSegment("search.php").build()
        val request =
            okhttp3.Request.Builder().url(url).post(FormBody.Builder().add("q", query).build())
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = response.body?.string()

                    if (body.isNullOrBlank()) {
                        continuation.resume(null)
                        return
                    }

                    val regex = Regex("<a\\s.*?href=\"([^\"]*)\"")
                    val matchResult = regex.find(body)
                    if (matchResult != null) {
                        val link = matchResult.groups[1]?.value
                        println(link)

                        continuation.resume(link)
                    } else {
                        continuation.resume(null)
                    }
                }
            }
        })
    }

    suspend fun getMovie(path: String): ExMovie? = suspendCoroutine { continuation ->
        val moviePath = path.substringAfter(getScheme() + "://").substringAfter(getHost() + "/", "")
        val url =
            HttpUrl.Builder().scheme(getScheme()).host(getHost()).addPathSegments(moviePath).build()
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = response.body?.string()
                    if (body == null) {
                        continuation.resume(null)
                        return
                    }

                    val soup = Jsoup.parse(body)
                    val id = moviePath.substringAfterLast("/").substringBefore("-").toIntOrNull()
                    if (id == null) {
                        continuation.resume(null)
                        return
                    }

                    var translations =
                        soup.select("ul#translators-list > li.b-translator__item").map {
                            Translation(
                                it.attr("data-translator_id").toInt(),
                                it.text() + if (it.select("img").attr("src")
                                        .contains("ua.png")
                                ) " \uD83C\uDDFA\uD83C\uDDE6" else ""
                            )
                        }
                    if (translations.isEmpty()) {
                        translations = listOf(Translation(110, "Оригинал"))
                    }
                    val isSerial = soup.select("meta[property=og:type]").attr("content")
                        .contains("tv_series")

                    continuation.resume(
                        ExMovie(
                            id, translations, isSerial
                        )
                    )
                }
            }
        })
    }

    suspend fun getTrailer(movieId: Int): String? = suspendCoroutine { continuation ->
        val url = HttpUrl.Builder().scheme(getScheme()).host(getHost()).addPathSegment("engine")
            .addPathSegment("ajax").addPathSegment("gettrailervideo.php").build()

        val request = okhttp3.Request.Builder().url(url).post(
            FormBody.Builder().add("id", movieId.toString()).build()
        ).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = response.body?.string()
                    if (body == null) {
                        continuation.resume(null)
                        return
                    }
                    val json = JSONObject(body)
                    val result = if (json.getBoolean("success")) {
                        val code = json.getString("code")
                        val document = Jsoup.parse(code)
                        val iframe = document.select("iframe").first()
                        val src = iframe?.attr("src")
                        if (src != null) {
                            val videoId = src.substringAfterLast("/").substringBefore("?")
                            "https://youtu.be/$videoId"
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                    continuation.resume(result)
                }
            }
        })
    }

    private fun <T> product(a: Collection<T>?, r: Int): List<Collection<T>> {
        var result = Collections.nCopies<Collection<T>>(1, emptyList())
        for (pool in Collections.nCopies(r, LinkedHashSet<T>(a))) {
            val temp: MutableList<Collection<T>> = ArrayList()
            for (x in result) {
                for (y in pool) {
                    val z: MutableCollection<T> = ArrayList(x)
                    z.add(y)
                    temp.add(z)
                }
            }
            result = temp
        }
        return result
    }

    private fun clearTrash(data: String): String {
        val trashList = listOf("@", "#", "!", "^", "$")
        val trashCodesSet = mutableListOf<String>()

        for (i in 2..4) {
            val startchar = ""
            for (chars in product(trashList, i)) {
                val dataBytes = chars.joinToString(startchar).encodeToByteArray()
                val trashcombo = Base64.encodeToString(dataBytes, Base64.NO_WRAP)
                trashCodesSet.add(trashcombo)
            }
        }

        var trashString = data.replace("#h", "").split("//_//").joinToString("")
        for (i in trashCodesSet) {
            val temp = i.replace("\n", "") // Remove any newline characters from the Base64 string
            trashString = trashString.replace(temp, "")
        }

        val finalString = Base64.decode(trashString, Base64.NO_WRAP)
        return finalString.decodeToString()
    }


    suspend fun loadResolutions(
        movieId: Int, translationId: Int, season: Int? = null, episode: Int? = null
    ): List<Stream> = suspendCoroutine { continuation ->
        val form = FormBody.Builder().add("id", movieId.toString())
            .add("translator_id", translationId.toString())
        if (season != null) {
            form.add("season", season.toString()).add("episode", episode.toString())
                .add("action", "get_stream")
        } else {
            form.add("is_camrip", "0").add("is_ads", "0").add("is_director", "0")
                .add("action", "get_movie")
        }

        val url = HttpUrl.Builder().scheme(getScheme()).host(getHost()).addPathSegment("ajax")
            .addPathSegment("get_cdn_series").addPathSegment("")
            .addQueryParameter("t", System.currentTimeMillis().toString()).build()
        Log.d("API", url.toString())
        val request = okhttp3.Request.Builder().url(url).post(
            form.build()
        ).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val body = response.body?.string()
                    if (body == null) {
                        Log.e("API", "Empty response")
                        continuation.resume(emptyList())
                        return
                    }
                    val json = JSONObject(body)

                    if (!json.getBoolean("success")) {
                        Log.e("API", json.getString("message"))
                        continuation.resume(emptyList())
                        return
                    }

                    println("loadResolutions: $json")

                    val link = clearTrash(json.getString("url"))
                    val arr = link.split(",")

                    val subtitle = json.optString("subtitle", "")
                    if (subtitle.isEmpty() || subtitle.equals("false")) {
                        val result = arr.map {
                            val res = it.split("[")[1].split("]")[0]
                            val video = it.split("]")[1].split(" or ")[1]
                            Stream(video, res, emptyList())
                        }
                        continuation.resume(result)
                        return
                    }

                    val subtitleCodes = json.optJSONObject("subtitle_lns") ?: JSONObject()

                    val subtitles = subtitle.split(",").map {
                        val lang = it.substringAfter("[").substringBefore("]")
                        val subtitleUrl = it.substringAfter("]").substringAfter(" ")
                        val code = subtitleCodes.getString(lang)
                        Subtitle(subtitleUrl, code, lang)
                    }

                    val result = arr.map {
                        val res = it.substringAfter("[").substringBefore("]")
                        val video = it.substringAfter("]").substringAfter(" or ")
                        Stream(video, res, subtitles)
                    }
                    continuation.resume(result)
                }
            }
        })
    }

    suspend fun loadSeasonsForTranslation(movieId: Int, translationId: Int): List<Season> =
        suspendCoroutine { continuation ->
            val url = HttpUrl.Builder()
                .scheme(getScheme())
                .host(getHost())
                .addPathSegment("ajax")
                .addPathSegment("get_cdn_series")
                .addPathSegment("")
                .build()

            val request = okhttp3.Request.Builder()
                .url(url)
                .post(
                    FormBody.Builder()
                        .add("id", movieId.toString())
                        .add("translator_id", translationId.toString())
                        .add("favs", "0")
                        .add("action", "get_episodes")
                        .build()
                )
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val body = response.body?.string()
                        if (body == null) {
                            Log.e("API", "Empty response")
                            continuation.resume(emptyList())
                            return
                        }
                        val json = JSONObject(body)

                        if (!json.getBoolean("success")) {
                            Log.e("API", json.getString("message"))
                            continuation.resume(emptyList())
                            return
                        }

                        val soupSeasons = Jsoup.parse(json.getString("seasons"))
                        val soupEpisodes = Jsoup.parse(json.getString("episodes"))

                        val seasonsClasses = soupSeasons.select("li").map {
                            Season(it.attr("data-tab_id").toInt(), emptyList())
                        }

                        val episodesClasses = soupEpisodes.select("ul").map { ul ->
                            ul.select("li").map {
                                Episode(it.attr("data-episode_id").toInt())
                            }
                        }

                        seasonsClasses.forEachIndexed { index, season ->
                            season.episodes = episodesClasses[index]
                        }

                        continuation.resume(seasonsClasses)
                    }
                }
            })
        }
}
