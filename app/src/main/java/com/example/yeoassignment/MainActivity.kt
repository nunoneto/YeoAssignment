package com.example.yeoassignment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.yeoassignment.ui.theme.YeoAssignmentTheme
import com.example.yeoassignment.viewmodel.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ContactsViewModel by viewModels()

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.importContacts()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YeoAssignmentTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(id = R.string.app_name))
                            },
                            actions = {
                                IconButton(onClick = { importContacts() }) {
                                    Icon(Icons.Filled.Refresh, "")
                                }
                            }
                        )
                    },
                    content = {
                        ListView(viewModel)
                    }
                )
            }
        }
    }

    private fun importContacts() {
        if (hasReadContactsPermission()) {
            viewModel.importContacts()
        } else {
            requestReadContactsPermission()
        }
    }

    private fun requestReadContactsPermission() {
        permissionRequest.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun hasReadContactsPermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
}


@Composable
fun ListView(viewModel: ContactsViewModel) {
    val state = viewModel.contactsStateFlow.collectAsState(emptyList())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(1F)
            .padding(8.dp)
    ) {
        items(state.value.count()) {

            Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                val textModifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                Text(text = state.value[it].name, modifier = textModifier)
                Text(text = state.value[it].phoneNumbers.joinToString(), modifier = textModifier)
                Text(text = state.value[it].timestamp, modifier = textModifier)
            }
        }
    }
}
