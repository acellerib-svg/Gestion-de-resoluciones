import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  // ✅ NO enviar token al login (opcional pero recomendado)
  // OJO: si tu backend usa "http://localhost:8080/api/auth/login",
  // esta condición igual funciona porque incluye "/api/auth/login".
  if (req.url.includes('/api/auth/login')) {
    return next(req);
  }

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(req);
};
