import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // 1) Debe existir token (logueado)
  if (!auth.isLoggedIn()) {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  // 2) Verificar si el perfil esta completo
  const session = auth.getSession();
  if (session && session.profileCompleted === false && state.url !== '/completar-perfil') {
    router.navigate(['/completar-perfil']);
    return false;
  }

  // 3) Permisos requeridos (si la ruta los define)
  const required: string[] = route.data?.['permissions'] ?? [];

  if (required.length > 0 && !auth.hasAnyPermission(required)) {
    router.navigate(['/inicio']);
    return false;
  }

  return true;
};
