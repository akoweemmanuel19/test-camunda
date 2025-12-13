<template>
  <nav class="navbar">
    <div class="logo">Camunda Test App</div>
    <div v-if="isLoggedIn" class="user-info">
      Connecté en tant que **{{ user.personne.prenom }}** | 
      <button @click="handleLogout()" class="logout-btn">Déconnexion</button>
    </div>
  </nav>
</template>

<script setup>
import { useAuth } from '~/composables/useAuth';
import { useRouter } from 'vue-router';

const { user, isLoggedIn, logout } = useAuth();
const router = useRouter();

const handleLogout = () => {
    logout();
    router.push('/login');
}
</script>

<style scoped>
.navbar { display: flex; justify-content: space-between; align-items: center; padding: 15px 30px; background-color: #333; color: white; }
.logo { font-size: 1.5em; font-weight: bold; }
.logout-btn { background: none; border: 1px solid white; color: white; padding: 5px 10px; cursor: pointer; }
</style>