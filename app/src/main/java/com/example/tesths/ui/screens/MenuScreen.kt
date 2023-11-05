package com.example.tesths.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.tesths.R
import com.example.tesths.domain.model.Product
import com.example.tesths.ui.MainActivity
import com.example.tesths.ui.state.ProductsScreenState
import com.example.tesths.ui.theme.CustomDarkWhite
import com.example.tesths.ui.theme.CustomLightRed
import com.example.tesths.ui.theme.CustomRed
import com.example.tesths.ui.theme.CustomWhite
import com.example.tesths.ui.theme.Typography
import com.example.tesths.ui.viewmodel.ProductsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val categories = listOf("Пицца", "Комбо", "Десерты", "Напитки")
private val banners = listOf(R.drawable.banner_1, R.drawable.banner_2)

@Composable
fun MenuScreen(
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current

    val viewModel: ProductsViewModel = viewModel(
        factory = (LocalContext.current as MainActivity).viewModelFactory
    )

    val state = viewModel.productsScreenState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
    }

    val selectedCategory = rememberSaveable {
        mutableStateOf(categories[0])
    }
    var categoriesShadowVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val categoriesScrollState = rememberLazyListState()
    val menuScrollState = rememberLazyListState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                categoriesShadowVisible =
                    menuScrollState.canScrollBackward && menuScrollState.layoutInfo.visibleItemsInfo[0].offset == 0
                when (menuScrollState.firstVisibleItemIndex) {
                    0, 9 -> selectedCategory.value = categories[0]
                    10, 19 -> selectedCategory.value = categories[1]
                    20, 29 -> selectedCategory.value = categories[2]
                    30 -> selectedCategory.value = categories[3]
                }
                return Offset.Zero
            }
        }
    }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column {
            Headline()
            when (val currentState = state.value) {
                is ProductsScreenState.Loading -> {
                    LoadingPlaceholder()
                }

                is ProductsScreenState.Content -> {
                    MenuContent(
                        nestedScrollConnection = nestedScrollConnection,
                        menuScrollState = menuScrollState,
                        categoriesScrollState = categoriesScrollState,
                        selectedCategory = selectedCategory,
                        categoriesShadowVisible = categoriesShadowVisible,
                        scope = scope,
                        products = currentState.products
                    )
                }

                is ProductsScreenState.Error -> {
                    Toast.makeText(context, "error: ${currentState.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is ProductsScreenState.InternetError -> {
                    SideEffect {
                        scope.launch {
                            val snackBarResult = snackBarHostState.showSnackbar(
                                message = "Нет интернет-подключения",
                                actionLabel = "Обновить",
                                duration = SnackbarDuration.Indefinite
                            )
                            if (snackBarResult == SnackbarResult.ActionPerformed) {
                                viewModel.getProducts(true)
                            }
                        }
                    }
                    MenuContent(
                        nestedScrollConnection = nestedScrollConnection,
                        menuScrollState = menuScrollState,
                        categoriesScrollState = categoriesScrollState,
                        selectedCategory = selectedCategory,
                        categoriesShadowVisible = categoriesShadowVisible,
                        scope = scope,
                        products = currentState.products
                    )
                }
            }
        }
        NoInternetConnectionSnackBar(snackBarHostState)
    }
}

@Composable
private fun BoxScope.NoInternetConnectionSnackBar(snackBarHostState: SnackbarHostState) {
    SnackbarHost(
        hostState = snackBarHostState,
        modifier = Modifier.Companion
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
    ) {
        Snackbar(
            snackbarData = it,
            containerColor = CustomRed,
            contentColor = CustomWhite,
            actionColor = CustomWhite
        )
    }
}


@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun MenuContent(
    nestedScrollConnection: NestedScrollConnection,
    menuScrollState: LazyListState,
    categoriesScrollState: LazyListState,
    selectedCategory: MutableState<String>,
    categoriesShadowVisible: Boolean,
    scope: CoroutineScope,
    products: List<Product>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        state = menuScrollState
    ) {
        item {
            Banners(banners)

            Spacer(modifier = Modifier.height(8.dp))
        }
        stickyHeader {
            Categories(categories, selectedCategory, categoriesScrollState) {
                selectedCategory.value = it
                scope.launch {
                    val itemIndex = when (it) {
                        categories[0] -> 0
                        categories[1] -> 11
                        categories[2] -> 21
                        else -> 31
                    }
                    menuScrollState.scrollToItem(
                        itemIndex,
                        -categoriesScrollState.layoutInfo.viewportSize.height
                    )
                }
            }
            val alphaShadowAnimation by animateFloatAsState(
                targetValue = if (categoriesShadowVisible) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 300
                ), label = ""
            )
            if (categoriesShadowVisible) {
                CategoriesShadow(alphaShadowAnimation)
            }
        }
        items(products) {
            MenuItem(it)
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
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

@Composable
private fun CategoriesShadow(alphaShadowAnimation: Float) {
    Divider(
        modifier = Modifier
            .clip(GenericShape { size, _ ->
                lineTo(size.width, 0f)
                lineTo(size.width, Float.MAX_VALUE)
                lineTo(0f, Float.MAX_VALUE)
            })
            .shadow(
                elevation = 12.dp,
                spotColor = MaterialTheme.colorScheme.primary.copy(alphaShadowAnimation),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alphaShadowAnimation)
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
            error = { ImageErrorPlaceholder() },
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
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary
            )
            OutlinedButton(
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, CustomRed),
                onClick = { }) {
                Text(
                    text = String.format("от %d$", product.price),
                    style = Typography.bodySmall,
                    color = CustomRed
                )
            }
        }
    }
}

@Composable
private fun ImageErrorPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CustomDarkWhite)
    ) {
        Icon(
            modifier = Modifier.align(
                Alignment.Center
            ),
            painter = painterResource(id = R.drawable.ic_bottom_menu),
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = "Product image placeholder"
        )
    }
}

@Composable
private fun Categories(
    categories: List<String>,
    selectedCategory: MutableState<String>,
    categoriesScrollState: LazyListState,
    onCategoryClick: (String) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        state = categoriesScrollState
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory.value
            ) {
                onCategoryClick(category)
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
    banners: List<Int>,
) {
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(banners) {
            BannerItem(it)
        }
    }
}

@Composable
private fun BannerItem(
    bannerImageResId: Int,
) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(112.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(CustomRed)
    ) {
        Image(
            painter = painterResource(id = bannerImageResId),
            contentScale = ContentScale.Crop,
            contentDescription = "Banner image"
        )
    }
}

@Composable
fun ImageLoadingPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CustomDarkWhite)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(
                Alignment.Center
            ),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}