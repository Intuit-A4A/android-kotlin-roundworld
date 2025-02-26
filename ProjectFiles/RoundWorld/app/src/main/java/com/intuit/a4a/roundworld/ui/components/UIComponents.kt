package com.intuit.a4a.roundworld.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.intuit.a4a.roundworld.R
import com.intuit.a4a.roundworld.data.CountryRegion
import com.intuit.a4a.roundworld.data.CountryResponse

const val LOADING_SPINNER_TEST_TAG_NAME = "Loading"
const val COUNTRY_NAME_TEST_TAG_NAME = "Country"

/**
 * Composable function for the country list row, showing flag image on the left and
 * country info on the right
 * @param item: Network response represented by [CountryResponse]
 */
@Composable
internal fun CountryListRowComponent(
    item: CountryResponse,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.color_container_background_primary)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountryFlagImage(
                imgUrl = item.flags?.svg ?: "",
                imgContentDescription = item.name?.common ?: "flag",
                imgPlaceHolder = R.drawable.ic_launcher_foreground
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .testTag(COUNTRY_NAME_TEST_TAG_NAME),
                    text = item.name?.common?: "",
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .testTag(COUNTRY_NAME_TEST_TAG_NAME),
                    text = "Capital: ${item.capital.firstOrNull()}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

/**
 * Composable function showing the flag image through Coil's rememberAsyncImagePainter API
 * @param imageHeight: height on the image, default is 100.dp
 * @param imgUrl: URL of the image to show
 * @param imgContentDescription: Content description of the image
 * @param isDisplayedInRow: Boolean flag default to true to indicate if the image is
 * shown in a list row, which uses ContentScale.Inside scale. Otherwise, the scale is FillBounds
 * @param imgPlaceHolder: Image place holder
 */
@Composable
fun CountryFlagImage(
    imageHeight: Dp = 100.dp,
    imgUrl: String,
    imgContentDescription: String,
    isDisplayedInRow: Boolean = true,
    @DrawableRes imgPlaceHolder: Int? = null
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(16.dp)
            .height(imageHeight)
            .width(imageHeight),
    ) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)
            .apply {
                imgPlaceHolder?.let { placeholder(it) }
            }
            .build()

        Image(
            painter = rememberAsyncImagePainter(model = imageRequest),
            contentDescription = imgContentDescription,
            contentScale = if (isDisplayedInRow) ContentScale.Inside else ContentScale.FillBounds
        )
    }
}

/**
 * Compose function for rendering drop down menu for country region selection
 * @param regionList: List of country regions to show
 * @param onItemClicked: Lambda to trigger when item in the dropdown is selected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionDropDown(
    regionList: List<CountryRegion>,
    onItemClicked: (CountryRegion) -> Unit,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var selectedRegion by rememberSaveable { mutableStateOf(regionList[0].region) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = !isExpanded
            }
        ) {
            TextField(
                value = selectedRegion,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            
            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                regionList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(
                            text = item.region
                        ) },
                        onClick = {
                            selectedRegion = item.region
                            isExpanded = false
                            onItemClicked(item)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Composable function rendering loading screen (with a indefinite circular progress indicator)
 */
@Composable
fun LoadingScreenUI() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(color = colorResource(id = R.color.color_container_background_primary)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(64.dp)
                .testTag(LOADING_SPINNER_TEST_TAG_NAME),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

    }
}

/**
 * Compose function for render error state UI function of the list screen
 * It shows the error message in the center of the screen
 */
@Composable
fun ErrorStateUI(exception: Throwable) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Error loading country list, error is ${exception.message}"
        )
    }
}
