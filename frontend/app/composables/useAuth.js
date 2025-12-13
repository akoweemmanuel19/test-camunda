// composables/useAuth.js
import { useState } from '#app';

// État global pour l'utilisateur connecté
export const useAuth = () => {
  const user = useState('auth_user', () => null); // Stocke l'objet Utilisateur
  const isLoggedIn = useState('auth_isLoggedIn', () => false);

  const login = (userData) => {
    user.value = userData;
    isLoggedIn.value = true;
  };

  const logout = () => {
    user.value = null;
    isLoggedIn.value = false;
  };

  return { user, isLoggedIn, login, logout };
};