package com.example.filmus.ui.screens.profile

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.filmus.R
import com.example.filmus.common.Constants
import com.example.filmus.domain.UIState
import com.example.filmus.repository.TokenManager
import com.example.filmus.ui.fields.CustomDateField
import com.example.filmus.ui.fields.CustomTextField
import com.example.filmus.ui.fields.GenderSelection
import com.example.filmus.ui.navigation.Screen
import com.example.filmus.viewmodel.profile.ProfileViewModel
import com.example.filmus.viewmodel.profile.ProfileViewModelFactory

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    navController: NavController, tokenManager: TokenManager
) {
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(tokenManager))
    val vibratorManager =
        LocalContext.current.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator

    val nickname by viewModel.nickname
    var email by viewModel.email
    var link by viewModel.avatarLink
    var name by viewModel.name
    var gender by viewModel.gender
    var birthDate by viewModel.birthDate
    var buttonEnabled by remember { mutableStateOf(false) }
    var screenState by viewModel.screenState
    val pullRefreshState = rememberPullRefreshState(screenState == UIState.LOADING, {
        viewModel.getInfo(true)
    })
    val mContext = LocalContext.current
    val imageLoader = ImageLoader.Builder(mContext).components {
        add(ImageDecoderDecoder.Factory())
    }.memoryCache {
        MemoryCache.Builder(mContext).maxSizePercent(0.25).build()
    }.diskCache {
        DiskCache.Builder().directory(mContext.cacheDir.resolve("image_cache")).maxSizePercent(0.02)
            .build()
    }.build()

    var enlargedImage by remember { mutableStateOf(false) }
    val showDialog = remember { mutableStateOf(false) }
    val nameValidationState = viewModel.validationStates.value.getOrNull(0)
    val emailValidationState = viewModel.validationStates.value.getOrNull(1)
    val birthDateValidationState = viewModel.validationStates.value.getOrNull(2)
    LaunchedEffect(Unit) {
        viewModel.getInfo()
    }
    when (viewModel.screenState.value) {
        UIState.ERROR -> {
            Toast.makeText(mContext, Constants.UNKNOWN_ERROR, Toast.LENGTH_SHORT).show()
            screenState = UIState.DEFAULT
        }

        UIState.UNAUTHORIZED -> {
            Toast.makeText(mContext, Constants.UNAUTHORIZED_ERROR, Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
            screenState = UIState.DEFAULT
        }

        else -> {}
    }

    Box(Modifier.pullRefresh(pullRefreshState)) {
        if (screenState == UIState.LOADING) {
            ProfilePlaceholder()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    val longPressDetector = Modifier.combinedClickable(onLongClick = {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                Constants.VIBRATION_BUTTON_CLICK,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        enlargedImage = true
                    }, onClick = {
                        enlargedImage = false
                    })
                    SubcomposeAsyncImage(
                        model = if (!enlargedImage) link else Constants.EMPTY,
                        imageLoader = imageLoader,
                        contentDescription = null,
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent, shape = CircleShape)
                            .then(longPressDetector),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Success -> {
                                SubcomposeAsyncImageContent()
                            }

                            is AsyncImagePainter.State.Error -> {
                                Image(
                                    painter = painterResource(id = R.drawable.anonymous),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(CircleShape)
                                        .background(Color.Transparent, shape = CircleShape)
                                )
                            }

                            is AsyncImagePainter.State.Empty -> {
                                Image(
                                    painter = painterResource(id = R.drawable.anonymous),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(88.dp)
                                        .clip(CircleShape)
                                        .background(Color.Transparent, shape = CircleShape)
                                )
                            }

                            is AsyncImagePainter.State.Loading -> {
                                CircularProgressIndicator(
                                    color = Color(0xFFFC315E),
                                    trackColor = Color(0x1AFC315E)
                                )
                            }
                        }
                    }
                }


                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = nickname,

                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    ), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        showDialog.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                    ),

                    ) {
                    Text(
                        text = "Выйти из аккаунта",

                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFC315E),

                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Электронная почта",

                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))


                CustomTextField(
                    onValueChange = {
                        if (!buttonEnabled) buttonEnabled = true
                        if (emailValidationState != null && !emailValidationState.isValid) {
                            emailValidationState.isValid = true
                        }
                        email = it
                    },
                    textFieldValue = email,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    containerColor = Color(viewModel.getContainerColor(emailValidationState)),
                    outlinedColor = Color(viewModel.getOutlineColor(emailValidationState)),
                    isPassword = false,
                    vibrator = vibrator
                )
                if (emailValidationState != null && !emailValidationState.isValid) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = emailValidationState.errorMessage, style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFE64646),
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Ссылка на аватар", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    onValueChange = {
                        if (!buttonEnabled) buttonEnabled = true
                        link = it
                    },
                    textFieldValue = link,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    isPassword = false,
                    vibrator = vibrator
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Имя", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    onValueChange = {
                        if (!buttonEnabled) buttonEnabled = true
                        if (nameValidationState != null && !nameValidationState.isValid) {
                            nameValidationState.isValid = true
                        }
                        name = it
                    },
                    textFieldValue = name,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    containerColor = Color(viewModel.getContainerColor(nameValidationState)),
                    outlinedColor = Color(viewModel.getOutlineColor(nameValidationState)),
                    isPassword = false,
                    vibrator = vibrator
                )
                if (nameValidationState != null && !nameValidationState.isValid) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = nameValidationState.errorMessage, style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFE64646),
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Пол",

                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                GenderSelection(
                    defaultIsMale = viewModel.gender, onGenderSelected = { selectedGender ->
                        gender = selectedGender
                        if (!buttonEnabled) buttonEnabled = true
                    }, vibrator = vibrator
                )
                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Дата рождения", style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomDateField(
                    onValueChange = {
                        if (!buttonEnabled) buttonEnabled = true
                        if (birthDateValidationState != null && !birthDateValidationState.isValid) {
                            birthDateValidationState.isValid = true
                        }
                        birthDate = it
                    },
                    textFieldValue = birthDate,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    containerColor = Color(
                        viewModel.getContainerColor(
                            birthDateValidationState
                        )
                    ),
                    outlinedColor = Color(viewModel.getOutlineColor(birthDateValidationState)),
                    vibrator = vibrator
                )
                if (birthDateValidationState != null && !birthDateValidationState.isValid) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = birthDateValidationState.errorMessage, style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFFE64646),
                        )
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                Constants.VIBRATION_BUTTON_CLICK,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        viewModel.validateProfileData()

                        if (viewModel.validationStates.value.all { it.isValid }) {
                            viewModel.updateInfo(onResult = { result ->
                                if (result) {
                                    Toast.makeText(
                                        mContext,
                                        Constants.UPDATE_DATA_SUCCESS,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    buttonEnabled = false
                                }
                            })
                        } else {
                            vibrator.vibrate(
                                VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .alpha(
                            if (buttonEnabled) 1f else 0.45f
                        ),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFC315E),
                        disabledContainerColor = Color(0xFFFC315E),
                    ),
                    enabled = buttonEnabled,

                    ) {
                    Text(
                        text = "Сохранить",

                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                Constants.VIBRATION_BUTTON_CLICK,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        viewModel.getInfo()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(size = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF292929),
                        disabledContainerColor = Color(0xFF292929),
                    ),

                    ) {
                    Text(
                        text = "Отмена",

                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFC315E),
                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        PullRefreshIndicator(
            screenState == UIState.LOADING,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )

    }
    if (showDialog.value) {
        AlertDialog(onDismissRequest = { showDialog.value = false }, title = {
            Text(
                text = "Выйти из аккаунта?", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                )
            )
        }, text = {
            Text(
                text = "Вы уверены, что хотите выйти из аккаунта?", style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFFFFFFFF),
                )
            )
        }, confirmButton = {
            ClickableText(
                AnnotatedString("Выйти"), onClick = {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            Constants.VIBRATION_BUTTON_CLICK, VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                    viewModel.logout(onResult = { result ->
                        if (result) {
                            Toast.makeText(
                                mContext,
                                Constants.LOGOUT_MESSAGE,
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(0)
                            }
                        }
                    })
                    showDialog.value = false

                }, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFC315E),
                ), modifier = Modifier.padding(10.dp)
            )
        }, dismissButton = {
            ClickableText(
                AnnotatedString("Отмена"), onClick = {
                    showDialog.value = false
                }, style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.inter)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF909499),
                ), modifier = Modifier.padding(10.dp)
            )
        }, containerColor = Color(0xFF1F1F1F), shape = RoundedCornerShape(10.dp)
        )
    }
    if (enlargedImage) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f)),
            contentAlignment = Alignment.Center
        ) {
            Scrim({ enlargedImage = false }, Modifier.fillMaxSize())
            AsyncImage(
                model = link,
                imageLoader = imageLoader,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent, shape = CircleShape),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}


// https://developer.android.com/jetpack/compose/touch-input/pointer-input/tap-and-press#dismiss-composable
@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics(mergeDescendants = true) {
                contentDescription = ""
                onClick {
                    onClose()
                    true
                }
            }
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose()
                    true
                } else {
                    false
                }
            }
            .background(Color.DarkGray.copy(alpha = 0.75f)))
}