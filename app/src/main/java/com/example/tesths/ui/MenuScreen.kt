package com.example.tesths.ui

import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.tesths.R
import com.example.tesths.domain.model.Product
import com.example.tesths.ui.state.ProductsScreenState
import com.example.tesths.ui.theme.CustomDarkGrey
import com.example.tesths.ui.theme.CustomLightRed
import com.example.tesths.ui.theme.CustomRed
import com.example.tesths.ui.theme.Typography
import com.example.tesths.ui.viewmodel.ProductsViewModel

private val categories = listOf("Пицца", "Комбо", "Десерты", "Напитки")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    paddingValues: PaddingValues,
) {
    val viewModel: ProductsViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
    }

    val selectedCategory = remember {
        mutableStateOf(categories[0])
    }
    var categoriesShadowVisible by remember {
        mutableStateOf(false)
    }
    val bannersScrollState = rememberLazyListState()
    val menuScrollState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                categoriesShadowVisible =
                    menuScrollState.canScrollBackward && menuScrollState.layoutInfo.visibleItemsInfo[0].offset == 0
                return Offset.Zero
            }
        }
    }
    val context = LocalContext.current
    val state = viewModel.productsScreenState.collectAsState()

    Column(
        Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Headline()
        when (val currentState = state.value) {
            is ProductsScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp)
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }

            is ProductsScreenState.Content -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(nestedScrollConnection),
                    state = menuScrollState
                ) {
                    item {
                        Banners(bannersScrollState)

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    stickyHeader {
                        Categories(categories, selectedCategory)
                        if (categoriesShadowVisible) {
                            CategoriesShadow()
                        }
                    }
                    items(currentState.products) {
                        MenuItem(it)
                    }
                }
            }

            is ProductsScreenState.Error -> {
                Toast.makeText(context, "error: ${currentState.error}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun CategoriesShadow() {
    Divider(
        modifier = Modifier
            .shadow(
                elevation = 12.dp,
                spotColor = CustomDarkGrey,
                ambientColor = CustomDarkGrey
            ),
        thickness = 0.dp,
    )
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
fun MenuItem(
    product: Product,
) {
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
        SubcomposeAsyncImage(
            modifier = Modifier
                .weight(0.48f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            model = product.image,
            contentScale = ContentScale.Crop,
            loading = { ImageLoadingPlaceholder() },
            contentDescription = "Item image"
        )
        Column(
            modifier = Modifier.weight(0.52f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.title,
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.weight(1f),
                text = product.description,
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, CustomRed),
                onClick = { }) {
                Text(
                    text = product.price.toString(),
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
    selectedCategory: MutableState<String>,
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
    scrollState: LazyListState,
) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = scrollState
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

@Composable
fun ImageLoadingPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.tertiary)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(
                Alignment.Center
            ),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}