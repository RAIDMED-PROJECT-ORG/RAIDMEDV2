<script setup lang="ts">
import { ref } from 'vue';
import AuthenticatedPageLayout from '@/layouts/AuthenticatedViewLayout.vue';
import ActionButton from '@/components/actionButton/ActionButton.vue';
import {
  faEarListen,
  faFileMedical,
  faGavel,
  faHandHoldingMedical,
  faMagnifyingGlass,
  faPerson,
  faPersonCircleQuestion,
  faPersonRays,
  faStethoscope,
  faSyringe
} from '@fortawesome/free-solid-svg-icons';
import { faSquarePlus } from '@fortawesome/free-regular-svg-icons';
import { getTypeActionDisplayName, TypeAction } from '@/models/virtual-patient/typeAction.enum';
import router from '@/router';
import { Color } from '@/models/new-patient/color.model';
import CharacteristicModal from '@/components/modal/characteristicModal/CharacteristicModal.vue';
import { initializeNewPatient, type NewPatient } from '@/models/new-patient/newPatient.model';
import type { Characteristics } from '@/models/new-patient/characteristics.model';
import WarningModal from '@/components/modal/warningModal/WarningModal.vue';
import type { Question } from '@/models/question/question.model';
import QuestionModal from '@/components/modal/questionModal/QuestionModal.vue';

const isCharacteristicModalOpen = ref(false);
const isQuestionModalOpen = ref(false);
const isWarningModalOpen = ref(false);
const errors = ref<string[]>([]);
const newPatient = ref<NewPatient>(initializeNewPatient());

function handleSubmit() {
  errors.value = [];
  if (!newPatient.value.characteristic) {
    errors.value.push('Caractéristiques du patient');
  }

  if (!newPatient.value.questions) {
    errors.value.push('Questions ouvertes ou fermées');
  }

  if (errors.value.length > 0) {
    switchWarningModalVisibility();
    return;
  }

  router.back();
}

function switchWarningModalVisibility() {
  isWarningModalOpen.value = !isWarningModalOpen.value;
}

function onCharacteristicValidation(data: Characteristics) {
  newPatient.value.characteristic = data;
  switchCharacteristicModalVisibility();
}

function switchCharacteristicModalVisibility() {
  isCharacteristicModalOpen.value = !isCharacteristicModalOpen.value;
}

function onQuestionValidation(data: Question[]) {
  newPatient.value.questions = data;
  switchQuestionModalVisibility();
}

function switchQuestionModalVisibility() {
  isQuestionModalOpen.value = !isQuestionModalOpen.value;
}
</script>

<template>
  <AuthenticatedPageLayout>
    <WarningModal
      v-if="isWarningModalOpen"
      :on-back="switchWarningModalVisibility"
      :errors="errors"
    />
    <CharacteristicModal
      v-if="isCharacteristicModalOpen"
      :onValidation="onCharacteristicValidation"
      :onBack="switchCharacteristicModalVisibility"
    />
    <QuestionModal
      v-if="isQuestionModalOpen"
      :onValidation="onQuestionValidation"
      :onBack="switchQuestionModalVisibility"
    />
    <div class="w-full h-full flex flex-col justify-center items-center">
      <h1 class="text-2xl text-primary font-bold">Nouveau patient</h1>
      <p class="text-center pt-3 w-1/2">
        Pour créer un nouveau cas de patient, cliquer sur chacune des catégories et remplir les
        champs demandés. Ensuite, valider la création du patient en cliquant sur le bouton “Créer le
        patient”. Il sera ensuite visible dans votre liste des patients.
      </p>
      <div class="flex gap-8 my-8">
        <div class="flex flex-col w-1/3">
          <ActionButton
            label="Caractéristiques du patient*"
            :color="Color.Red"
            :icon="faPerson"
            :onClick="switchCharacteristicModalVisibility"
          />
          <ActionButton label="Écouter" :color="Color.Blue" :icon="faEarListen" :onClick="switchQuestionModalVisibility"/>
          <ActionButton label="Question" :color="Color.Blue" :icon="faPersonCircleQuestion" />
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.SPECIFY_SYMPTOM)"
            :color="Color.Blue"
            :icon="faSquarePlus"
          />
        </div>
        <div class="flex flex-col w-1/3">
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.INSPECTION)"
            :color="Color.Orange"
            :icon="faMagnifyingGlass"
          />
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.PALPATATION)"
            :color="Color.Orange"
            :icon="faHandHoldingMedical"
          />
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.PERCUSSION)"
            :color="Color.Orange"
            :icon="faGavel"
          />
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.AUSCULTATION)"
            :color="Color.Orange"
            :icon="faStethoscope"
          />
        </div>
        <div class="flex flex-col w-1/3">
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.BIOLOGY_MICROBIOLOGY_PRESCRIPTION)"
            :color="Color.Purple"
            :icon="faFileMedical"
          />
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.IMAGING_PRESCRIPTION)"
            :color="Color.Purple"
            :icon="faPersonRays"
          />
          <ActionButton
            :label="getTypeActionDisplayName(TypeAction.BIOPSIES_PRESCRIPTION)"
            :color="Color.Purple"
            :icon="faSyringe"
          />
        </div>
      </div>
      <p>* Champs requis</p>
      <div>
        <ActionButton
          :disabled="newPatient.characteristic === null"
          class="mt-8"
          :onClick="handleSubmit"
          label="Créer le patient"
          :color="Color.Green"
        />
        <ActionButton
          class="mt-8"
          :onClick="() => router.back()"
          label="Annuler"
          :color="Color.Grey"
        />
      </div>
    </div>
  </AuthenticatedPageLayout>
</template>

<style scoped></style>
