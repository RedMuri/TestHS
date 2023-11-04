package com.example.tesths

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tesths.ui.theme.CustomLightRed
import com.example.tesths.ui.theme.CustomRed
import com.example.tesths.ui.theme.Typography

@Preview
@Composable
private fun Preview() {
    MenuScreenRoute()
}

@Composable
fun MenuScreenRoute() {

    MenuScreen()
}

private val categories = listOf("Пицца", "Комбо", "Десерты", "Напитки")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen() {
    var selectedCategory = remember {
        mutableStateOf(categories[0])
    }

    Column(
        Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Headline()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Banners()

                Spacer(modifier = Modifier.height(8.dp))
            }
            stickyHeader {
                Categories(categories,selectedCategory)
            }
            repeat(5) {
                item {
                    MenuItem()
                }
            }
        }
    }
}

@Composable
private fun Headline() {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Москва",
            style = Typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(9.dp))
        Icon(modifier = Modifier
            .size(24.dp)
            .clickable { }
            .padding(6.dp),
            painter = painterResource(id = R.drawable.ic_arrow_down),
            contentDescription = "Icon arrow down",
            tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable { },
            painter = painterResource(id = R.drawable.ic_qr),
            contentDescription = "Icon qr",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MenuItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.tertiary.copy(0.3f))
    )
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Image(
            modifier = Modifier
                .weight(0.48f)
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_bottom_menu),
            contentDescription = "Item image"
        )
        Column(
            modifier = Modifier.weight(0.52f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "item",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "item",
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, CustomRed),
                onClick = { }) {
                Text(
                    text = "item",
                    style = Typography.bodySmall,
                    color = CustomRed
                )
            }
        }
    }
}

@Composable
private fun Categories(
    categories: List<String>,
    selectedCategory: MutableState<String>
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory.value
            ) {
                selectedCategory.value = it
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean,
    onCategoryClick: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = { onCategoryClick(category) }
    ) {
        Text(
            modifier = Modifier
                .background(if (isSelected) CustomLightRed else MaterialTheme.colorScheme.background)
                .padding(vertical = 8.dp, horizontal = 24.dp),
            text = category,
            style = Typography.bodySmall,
            color = if (isSelected) CustomRed else MaterialTheme.colorScheme.tertiary
        )
    }
}


@Composable
fun Banners(
) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3) {
            item {
                BannerItem()
            }
        }
    }
}

@Composable
private fun BannerItem() {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(112.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(CustomRed)
    )
}
