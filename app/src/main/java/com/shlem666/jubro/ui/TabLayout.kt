package com.shlem666.jubro.ui

import android.util.Log
import androidx.compose.foundation.OverscrollEffect
import com.shlem666.jubro.feature.settings.R
import com.shlem666.jubro.ui.TabsUiState.Loading
import com.shlem666.jubro.ui.TabsUiState.Success
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.overscroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun TabLayout(
    modifier: Modifier = Modifier,
    viewModel: TabLayoutViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val tabsUiState by viewModel.tabsUiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.background(Color.White)
    ) {
        when (tabsUiState) {
            is Loading -> {
                Text(stringResource(R.string.loading))
            }
            is Success -> {
                val tabLayoutResources = (tabsUiState as Success).tabLayoutResources
                val pagerState = viewModel.pagerState
                Tabs(
                    pagerState = pagerState,
                    scrollState = scrollState,
                    tabsUrls = tabLayoutResources.tabsUrls,
                    viewModel = viewModel,
                )
                TabsContent(
                    pagerState = pagerState,
                    viewModel = viewModel,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tabs(
    pagerState: PagerState,
    scrollState: ScrollState,
    tabsUrls: MutableList<String>,
    viewModel: TabLayoutViewModel,
) {
    val scope = rememberCoroutineScope()
    Row (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        SecondaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            scrollState = scrollState,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            edgePadding = 0.dp,
        ) {
            tabsUrls.forEachIndexed { index, item ->
                Tab(
                    modifier = Modifier,
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                                //.padding(start = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            //horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text ="   Tab $index   ",
                                textAlign = TextAlign.Right,
                                modifier = Modifier
                                    //.background(Color.White)
                                    //.weight(1f)
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                            )
                            IconButton(
                                onClick = {
                                    viewModel.removeTabsUrls(index)
                                    if (pagerState.currentPage == tabsUrls.size - 1) {
                                        scope.launch {
                                            pagerState.scrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    //.background(Color.White)
                                    .height(20.dp)
                                    .width(20.dp)
                                    //.weight(1f),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun TabsContent(
    pagerState: PagerState,
    viewModel: TabLayoutViewModel,
) {
    val tabsUiState by viewModel.tabsUiState.collectAsStateWithLifecycle()
    when (tabsUiState) {
        is Loading -> {
            Text(text = stringResource(R.string.loading))
        }
        is Success -> {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                val webView = viewModel.webViews[page]
                key (webView) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (page < (tabsUiState as Success).tabLayoutResources.tabsUrls.size) {
                            Text(
                                text = (tabsUiState as Success).tabLayoutResources.tabsUrls[page],
                                modifier = Modifier.align(Alignment.Start),
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.loading),
                                modifier = Modifier.align(Alignment.Start),
                            )
                        }
                        AndroidView(
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(3f) // !!! Cures bug with tab blinking !!!
                            ,
                            factory = { webView }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun TabLayoutPreview() {
    TabLayout()
}