package com.telesoftas.newsapp.ui.screens.newsdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.telesoftas.newsapp.data.networking.response.Article
import com.telesoftas.newsapp.ui.Screen

@Composable
fun NewsDetailsScreen(
    article: Article,
    navController: NavController,
) {
    Column {
        AsyncImage(
            model = article.urlToImage,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = article.title.orEmpty(),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.align(Alignment.End),
                text = article.author.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
            )

            Text(
                modifier = Modifier.align(Alignment.End),
                text = article.publishedAt.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
            )

            Button(
                onClick = {
                    navController.navigate(Screen.WebView(article.url.orEmpty()))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Read Full Article Online".uppercase(),
                )
            }
        }
    }
}
