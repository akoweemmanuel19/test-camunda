<template>
  <div class="dashboard-page">
    <Navbar />
    
    <div v-if="!isLoggedIn" class="not-logged-in-message">
      Veuillez vous <NuxtLink to="/login">connecter</NuxtLink> pour accéder au tableau de bord.
    </div>

    <div v-else class="content-area">
      <h1>Bienvenue, {{ user.personne.prenom }} ({{ user.personne.poste }})</h1>
      <p>ID de la Personne : {{ user.personne.id }}</p>

      <template v-if="user.personne.poste === 'DEVELOPPEUR' || user.personne.poste === 'ANALYSTE'">
        <h2>Espace Employé : Mes Demandes de Congés</h2>
        <EmployeeDashboard />
      </template>

      <template v-if="user.personne.poste === 'MANAGER'">
        <h2>Espace Manager : Demandes en Attente d'Approbation</h2>
        <ManagerDashboard />
      </template>
    </div>
  </div>
</template>

<script setup>
import { useAuth } from '~/composables/useAuth';

const { user, isLoggedIn } = useAuth();
</script>