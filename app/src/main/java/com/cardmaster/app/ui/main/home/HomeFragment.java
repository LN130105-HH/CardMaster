package com.cardmaster.app.ui.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.FragmentHomeBinding;
import com.cardmaster.app.ui.deck.DeckDetailActivity;
import com.cardmaster.app.ui.main.MainActivity;
import com.cardmaster.app.ui.main.storage.adapter.DeckAdapter;
import com.cardmaster.app.ui.notification.NotificationActivity;
import com.cardmaster.app.ui.planner.StudyPlannerActivity;
import com.cardmaster.app.ui.planner.adapter.StudyPlanAdapter;
import com.cardmaster.app.ui.quiz.QuizSetupActivity;
import com.cardmaster.app.ui.study.FlashcardStudyActivity;
import com.cardmaster.app.util.SessionManager;

import java.util.List;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment implements DeckAdapter.OnDeckActionListener {

    private FragmentHomeBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private DeckAdapter recentDeckAdapter;
    private StudyPlanAdapter upcomingPlanAdapter;
    private DeckEntity currentContinueDeck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new AppRepository(requireActivity().getApplication());
        sessionManager = new SessionManager(requireContext());

        binding.tvGreeting.setText("Xin chào, " + sessionManager.getUserName());

        setupAdapters();
        observeData();
        setupListeners();
    }

    private void setupAdapters() {
        recentDeckAdapter = new DeckAdapter(requireContext(), this);
        binding.rvRecentDecks.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentDecks.setAdapter(recentDeckAdapter);

        upcomingPlanAdapter = new StudyPlanAdapter(requireContext(), plan -> {
            startActivity(new Intent(requireContext(), StudyPlannerActivity.class));
        });
        binding.rvUpcomingPlans.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUpcomingPlans.setAdapter(upcomingPlanAdapter);
    }

    private void observeData() {
        long userId = sessionManager.getUserId();

        // Total cards overview
        repository.getTotalCardsLive(userId).observe(getViewLifecycleOwner(), total -> {
            binding.tvTotalCards.setText(String.valueOf(total != null ? total : 0));
        });
        repository.getMemorizedCardsLive(userId).observe(getViewLifecycleOwner(), memorized -> {
            binding.tvMemorizedCards.setText(String.valueOf(memorized != null ? memorized : 0));
        });
        repository.getNeedReviewCardsLive(userId).observe(getViewLifecycleOwner(), needReview -> {
            binding.tvNeedReviewCards.setText(String.valueOf(needReview != null ? needReview : 0));
        });

        // Notifications unread badge
        repository.getUnreadNotificationCountLive(userId).observe(getViewLifecycleOwner(), unread -> {
            if (unread != null && unread > 0) {
                binding.tvNotifBadge.setVisibility(View.VISIBLE);
                binding.tvNotifBadge.setText(String.valueOf(unread));
            } else {
                binding.tvNotifBadge.setVisibility(View.GONE);
            }
        });

        // Recent decks
        repository.getDecksLive(userId).observe(getViewLifecycleOwner(), decks -> {
            if (decks != null && !decks.isEmpty()) {
                recentDeckAdapter.setDecks(decks.subList(0, Math.min(decks.size(), 3)));
                currentContinueDeck = decks.get(0);
                binding.cardContinueStudy.setVisibility(View.VISIBLE);
                binding.tvLastStudiedDeckTitle.setText(currentContinueDeck.getTitle());
            } else {
                binding.cardContinueStudy.setVisibility(View.GONE);
            }
        });

        // Upcoming plans
        repository.getUpcomingPlansLive(userId).observe(getViewLifecycleOwner(), plans -> {
            upcomingPlanAdapter.setPlans(plans);
        });
    }

    private void setupListeners() {
        binding.btnNotification.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), NotificationActivity.class));
        });

        binding.btnQuickStart.setOnClickListener(v -> {
            if (currentContinueDeck != null) {
                onStudyFlashcard(currentContinueDeck);
            }
        });

        binding.tvViewAllDecks.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToStorage();
            }
        });

        binding.tvManagePlans.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StudyPlannerActivity.class));
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
        onDeckClick(deck);
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
