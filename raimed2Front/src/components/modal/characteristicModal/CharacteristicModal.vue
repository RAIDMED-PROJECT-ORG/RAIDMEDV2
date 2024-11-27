<script setup lang="ts">
import { ref } from 'vue';
import { Color } from '@/models/new-patient/color.model';
import GenericModal from '@/components/modal/genericModal/GenericModal.vue';
import { faCakeCandles, faMarsAndVenus, faQuestion } from '@fortawesome/free-solid-svg-icons';
import IconLabel from '@/components/iconLabel/IconLabel.vue';
import DropdownButton from '@/components/dropdownButton/DropdownButton.vue';
import type { Characteristics } from '@/models/new-patient/characteristics.model';

const props = defineProps<{
  onValidation: (data: Characteristics) => void;
  onBack: () => void;
}>();

const sex = ref('Homme');
const age = ref<number | null>(null);
const diagnostic = ref('');

const ageErrorMessage = ref<string | null>(null);
const diagnosticErrorMessage = ref<string | null>(null);

function handleValidation() {
  if (diagnostic.value !== '') {
    diagnosticErrorMessage.value = null;
  } else {
    diagnosticErrorMessage.value = 'Champs requis.';
  }

  if (age.value !== null) {
    ageErrorMessage.value = null;
  } else {
    ageErrorMessage.value = 'Champs requis.';
  }

  if (diagnostic.value !== '' && age.value !== null) {
    props.onValidation({ sex: sex.value, age: age.value, diagnostic: diagnostic.value });
  }
}
</script>

<template>
  <GenericModal
    title="Inspection"
    validationLabel="Valider"
    :onValidation="handleValidation"
    :headerColor="Color.Red"
    :onBack="onBack"
  >
    <div class="flex gap-4 flex-col ml-8 mr-8">
      <div>
        <IconLabel :icon="faMarsAndVenus" text="Sexe*" />
        <DropdownButton :options="['Homme', 'Femme']" v-model="sex" />
      </div>
      <div>
        <div>
          <IconLabel :icon="faCakeCandles" text="Âge*" />
          <input
            type="number"
            v-model="age"
            placeholder="Insérer l'âge"
            class="px-4 py-2 border border-gray-300 rounded-lg text-gray-500 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-300 w-[150px]"
          />
        </div>
        <span class="text-red-500" v-if="ageErrorMessage">{{ ageErrorMessage }}</span>
      </div>
      <div>
        <div>
          <IconLabel :icon="faQuestion" text="Diagnostic final*" />
          <input
            type="text"
            v-model="diagnostic"
            placeholder="Insérer le diagnostic final"
            class="w-auto min-w-[350px] px-4 py-2 border border-gray-300 rounded-lg text-gray-500 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-gray-300"
          />
        </div>
        <span class="text-red-500" v-if="diagnosticErrorMessage">{{ diagnosticErrorMessage }}</span>
      </div>
      <span>* Champs requis</span>
    </div>
  </GenericModal>
</template>

<style scoped>
.text-red-500 {
  color: #f87171;
}
</style>
