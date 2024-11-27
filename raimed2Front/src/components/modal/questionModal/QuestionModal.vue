<script setup lang="ts">
import { Color } from '@/models/new-patient/color.model';
import GenericModal from '@/components/modal/genericModal/GenericModal.vue';
import type { Question } from '@/models/question/question.model';
import { faPen, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

const props = defineProps<{
  onValidation: (data: Question[]) => void;
  onBack: () => void;
}>();

function handleValidation() {}
</script>

<template>
  <GenericModal
    title="Questions"
    validationLabel="Valider"
    :onValidation="handleValidation"
    :headerColor="Color.Blue"
    :onBack="onBack"
  >
    <div class="flex">
      <div class="p-5 flex flex-col border-r border-1">
        <h3 class="self-center text-black font-bold text-xl">Liste des questions</h3>
        <div>
          <div class="flex justify-between border-b border-1 border-[#9CA3AF] text-black font-bold">
            <h4>Questions</h4>
            <h4>Actions</h4>
          </div>
          <div class="flex justify-between gap-10 items-center border-b border-1 border-[#E7E7E7]">
            <div>
              <span>Quel sont vos antécédents familiaux ?</span>
              <div class="flex items-center gap-2">
                <hr class="border-[#9CA3AF] w-[7%]" />
                De multiples cancers
              </div>
            </div>
            <div class="flex gap-2">
              <button class="rounded-full bg-[#D9D9D9] py-1 px-2">
                <FontAwesomeIcon :icon="faPen" class="w-4 h-4" />
              </button>
              <button class="rounded-full bg-[#D9D9D9] py-1 px-2">
                <FontAwesomeIcon :icon="faTrash" class="w-4 h-4" />
              </button>
            </div>
          </div>
          <div class="flex justify-between gap-10 items-center border-b border-1 border-[#E7E7E7]">
            <div>
              <span>Quel sont vos antécédents familiaux ?</span>
              <div class="flex items-center gap-2">
                <hr class="border-[#9CA3AF] w-[7%]" />
                De multiples cancers
              </div>
            </div>
            <div class="flex gap-2">
              <button class="rounded-full bg-[#D9D9D9] py-1 px-2">
                <FontAwesomeIcon :icon="faPen" class="w-4 h-4" />
              </button>
              <button class="rounded-full bg-[#D9D9D9] py-1 px-2">
                <FontAwesomeIcon :icon="faTrash" class="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
      <div class="p-5 flex flex-col">
        <h3 class="self-center text-black font-bold text-xl">Ajouter une question</h3>
        <div class="flex">
          <div class="flex flex-col">
            <label for="type">
              <FontAwesomeIcon :icon="faPen" class="w-3 h-3" />
              Type
            </label>
            <select id="type" class="border border-[#D6D6D6] rounded-[8px] p-2">
              <option value="1">Ouverte</option>
              <option value="2">Fermée</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </GenericModal>
</template>

<style scoped></style>
