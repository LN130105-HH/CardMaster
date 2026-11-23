package com.cardmaster.app.ui.main.storage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.FragmentStorageBinding;
import com.cardmaster.app.ui.ai.AiDocumentActivity;
import com.cardmaster.app.ui.deck.DeckDetailActivity;
import com.cardmaster.app.ui.deck.DeckEditorDialog;
import com.cardmaster.app.ui.deck.SubjectEditorDialog;
import com.cardmaster.app.ui.impexp.ImportActivity;
import com.cardmaster.app.ui.main.storage.adapter.DeckAdapter;
import com.cardmaster.app.ui.main.storage.adapter.SubjectChipAdapter;
import com.cardmaster.app.ui.quiz.QuizSetupActivity;
import com.cardmaster.app.ui.study.FlashcardStudyActivity;
import com.cardmaster.app.util.SessionManager;

import java.util.List;

public class StorageFragment extends Fragment implements DeckAdapter.OnDeckActionListener {

    private FragmentStorageBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private DeckAdapter deckAdapter;
    private SubjectChipAdapter chipAdapter;

    private long currentSubjectId = -1; // -1 = All
    private String currentQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStorageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new AppRepository(requireActivity().getApplication());
        sessionManager = new SessionManager(requireContext());

        setupAdapters();
        observeSubjects();
        loadDecks();
        setupListeners();
    }

    private void setupAdapters() {
        chipAdapter = new SubjectChipAdapter(requireContext(), subjectId -> {
            currentSubjectId = subjectId;
            loadDecks();
        });
        binding.rvSubjectChips.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvSubjectChips.setAdapter(chipAdapter);

        deckAdapter = new DeckAdapter(requireContext(), this);
        binding.rvDecks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDecks.setAdapter(deckAdapter);
    }

    private void observeSubjects() {
        long userId = sessionManager.getUserId();
        repository.getSubjectsLive(userId).observe(getViewLifecycleOwner(), subjects -> {
            chipAdapter.setSubjects(subjects);
        });
    }

    private void loadDecks() {
        long userId = sessionManager.getUserId();

        if (!currentQuery.isEmpty()) {
            repository.searchDecksLive(userId, currentQuery).observe(getViewLifecycleOwner(), this::updateDeckList);
        } else if (currentSubjectId > 0) {
            repository.getDecksBySubjectLive(userId, currentSubjectId).observe(getViewLifecycleOwner(), this::updateDeckList);
        } else {
            repository.getDecksLive(userId).observe(getViewLifecycleOwner(), this::updateDeckList);
        }
    }

    private void updateDeckList(List<DeckEntity> decks) {
        deckAdapter.setDecks(decks);
        if (decks == null || decks.isEmpty()) {
            binding.llEmptyState.setVisibility(View.VISIBLE);
            binding.rvDecks.setVisibility(View.GONE);
        } else {
            binding.llEmptyState.setVisibility(View.GONE);
            binding.rvDecks.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        binding.btnAddSubject.setOnClickListener(v -> {
            SubjectEditorDialog.show(requireContext(), repository, sessionManager.getUserId(), null, null);
        });

        binding.btnAddDeck.setOnClickListener(v -> {
            DeckEditorDialog.show(requireContext(), repository, sessionManager.getUserId(), null, null);
        });

        binding.btnEmptyCreateDeck.setOnClickListener(v -> {
            DeckEditorDialog.show(requireContext(), repository, sessionManager.getUserId(), null, null);
        });

        binding.cardImportFile.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ImportActivity.class));
        });

        binding.cardAiCreate.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AiDocumentActivity.class));
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s != null ? s.toString().trim() : "";
                loadDecks();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDeckClick(DeckEntity deck) {
        Intent intent = new Intent(requireContext(), DeckDetailActivity.class);
        intent.putExtra("DECK_ID", deck.getId());
        startActivity(intent);
    }

    @Override
    public void onStudyFlashcard(DeckEntity deck) {
        sessionManager.setLastStudiedDeckId(deck.getId());
        Intent intent = new Intent(requireContext(), FlashcardStudyActivity.class);
        intent.putExtra("DECK_ID", deck.getId());
        startActivity(intent);
    }

    @Override
    public void onStartQuiz(DeckEntity deck) {
        sessionManager.setLastStudiedDeckId(deck.getId());
        Intent intent = new Intent(requireContext(), QuizSetupActivity.class);
        intent.putExtra("DECK_ID", deck.getId());
        startActivity(intent);
    }

    @Override
    public void onEditDeck(DeckEntity deck) {
        DeckEditorDialog.show(requireContext(), repository, sessionManager.getUserId(), deck, null);
    }

    @Override
    public void onDeleteDeck(DeckEntity deck) {
        repository.deleteDeck(deck, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
