package com.example.searchrepo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    title: String? = null,
    message: String,
    onConfirm: () -> Unit,
    confirmText: String = "확인",
    confirmBtnColor: Color? = null,
    onDismiss: (() -> Unit)? = null,
    dismissText: String? = null,
    dismissBtnColor: Color? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            title?.let {
                Text(
                    it,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                message,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (dismissText != null) {
                    Button(
                        onClick = { onDismiss?.invoke() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = dismissBtnColor ?: Color.Unspecified,
                            contentColor = Color.White
                        )
                    ) {
                        Text(dismissText)
                    }
                }
                Button(
                    onClick = {

                        onConfirm.invoke()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = confirmBtnColor ?: Color.Unspecified,
                        contentColor = Color.White
                    )
                ) {
                    Text(confirmText)
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogPreview() {
    MaterialTheme {
        CustomDialog(
            onDismissRequest = {},
            onConfirm = {},
            confirmText = "확인",
            confirmBtnColor = MaterialTheme.colorScheme.primary,
            onDismiss = {},
            dismissText = "취소",
            dismissBtnColor = MaterialTheme.colorScheme.primary,
            title = "알림",
            message = "지울 내용이 없습니다.\n먼저 키워드를 입력해 보세요."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogPreview2() {
    MaterialTheme {
        CustomDialog(
            onDismissRequest = {},
            onConfirm = {},
            confirmText = "확인",
            confirmBtnColor = MaterialTheme.colorScheme.primary,
            title = "알림",
            message = "지울 내용이 없습니다.\n먼저 키워드를 입력해 보세요."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomDialogPreview3() {
    MaterialTheme {
        CustomDialog(
            onDismissRequest = {},
            onConfirm = {},
            confirmText = "확인",
            confirmBtnColor = MaterialTheme.colorScheme.primary,
            message = "지울 내용이 없습니다.\n먼저 키워드를 입력해 보세요."
        )
    }
}