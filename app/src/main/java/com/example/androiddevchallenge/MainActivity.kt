/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

var countDownTimer: CountDownTimer? = null

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {
    val timerMinute = remember { mutableStateOf(0) }
    val timerSecond1 = remember { mutableStateOf(0) }
    val timerSecond2 = remember { mutableStateOf(0) }

    countDownTimer = object : CountDownTimer(61 * 1000, 1000) {

        override fun onTick(millisUntilFinished: Long) {

            val day = millisUntilFinished / (1000 * 24 * 60 * 60)
            val hour =
                (millisUntilFinished - day * (1000 * 24 * 60 * 60)) / (1000 * 60 * 60)

            val minute =
                (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60)) / (1000 * 60)

            val second =
                (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000

            timerMinute.value = minute.toInt()
            if (second > 9) {
                timerSecond1.value = (second / 10).toInt()
                timerSecond2.value = (second % 10).toInt()
            } else {
                timerSecond1.value = 0
                timerSecond2.value = second.toInt()
            }
        }

        override fun onFinish() {}
    }

    Surface(color = MaterialTheme.colors.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_title),
                contentDescription = "",
                modifier = Modifier.width(260.dp),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier.size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bg),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "00:",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    // 秒数的两个数字
                    TextSecond(timer = timerSecond1)
                    TextSecond(timer = timerSecond2)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_restart),
                contentDescription = "",
                modifier = Modifier
                    .width(36.dp)
                    .clickable {
                    },
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(48.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_start),
                contentDescription = "",
                modifier = Modifier
                    .width(100.dp)
                    .clickable {
                        countDownTimer?.start()
                    },
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
fun TextSecond(timer: MutableState<Int>) {
    // 竖直方向上位移距离
    val offsetY = 80.dp

    // 值从0-9
//    val timer = remember { mutableStateOf(0) }

    // 偶数数字（0,2,4,6,8,）
    val evenAlpha = animateFloatAsState(targetValue = if (timer.value % 2 == 0) 1f else 0f)
    val evenOffset = if (timer.value % 2 == 0) {
        -offsetY * (1 - evenAlpha.value)
    } else {
        offsetY * (1 - evenAlpha.value)
    }

    // 奇数数字（1,3,5,7,9）
    val oddAloha = animateFloatAsState(targetValue = if (timer.value % 2 != 0) 1f else 0f)
    val oddOffset = if (timer.value % 2 != 0) {
        -offsetY * (1 - oddAloha.value)
    } else {
        offsetY * (1 - oddAloha.value)
    }

    Box(
        modifier = Modifier.width(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (timer.value % 2 == 0) {
                timer.value.toString()
            } else {
                if (timer.value - 1 < 0) {
                    "9"
                } else {
                    (timer.value - 1).toString()
                }
            },
            fontSize = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(evenAlpha.value)
                .offset(y = evenOffset)
        )

        Text(
            text = if (timer.value % 2 != 0) {
                timer.value.toString()
            } else {
                if (timer.value - 1 < 0) {
                    "9"
                } else {
                    timer.value.toString()
                }
            },
            fontSize = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(oddAloha.value)
                .offset(y = oddOffset)
        )
    }
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

// fun getFormatNumber(number: Int): String {
//        if (number>9)
// }
