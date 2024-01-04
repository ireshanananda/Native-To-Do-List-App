package com.dicoding.mynoteapps.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mynoteapps.database.Note
import com.dicoding.mynoteapps.repository.NoteRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getAllNotes(): LiveData<List<Note>> = mNoteRepository.getAllNotes()

    fun getCompletedNotes(note: Note, isChecked: Boolean): LiveData<List<Note>> {
        return mNoteRepository.getCompletedNotes()
    }

    fun updateNoteCompletedState(note: Note) {
        viewModelScope.launch {
            mNoteRepository.update(note)
        }
    }

}