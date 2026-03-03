import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize, switchMap, timeout } from 'rxjs/operators';
import { of } from 'rxjs';
import { AuthService, LoginResponse } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  username = '';
  password = '';
  showPassword = false;
  error: string | null = null;
  loading = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    if (this.auth.isLoggedIn()) {
      this.router.navigate(['/inicio']);
    }
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  login(): void {
    if (this.loading) return;

    const u = (this.username ?? '').trim();
    const p = (this.password ?? '').trim();

    if (!u || !p) {
      this.error = 'Completa usuario y contraseña';
      this.cdr.detectChanges();
      return;
    }

    this.error = null;
    this.loading = true;
    this.cdr.detectChanges();

    this.auth
      .login(u, p)
      .pipe(
        timeout(10000),
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (resp: LoginResponse) => {
          this.auth.saveSession(resp);
          if (!resp.profileCompleted) {
            this.router.navigate(['/completar-perfil']);
            return;
          }
          // Cargar instancias del usuario y permisos
          this.auth.loadMyInstances(resp.id).subscribe({
            next: (instances) => {
              if (instances.length === 1) {
                // Auto-seleccionar si solo tiene una instancia
                this.auth.loadMe(resp.id, instances[0].id).subscribe({
                  next: () => this.router.navigate(['/inicio']),
                  error: () => this.router.navigate(['/inicio']),
                });
              } else if (instances.length > 1) {
                // Guardar instancias disponibles para el selector
                localStorage.setItem('availableInstances', JSON.stringify(instances));
                this.router.navigate(['/seleccionar-instancia']);
              } else {
                // Sin instancias asignadas, ir a inicio
                this.router.navigate(['/inicio']);
              }
            },
            error: () => this.router.navigate(['/inicio']),
          });
        },
        error: (err) => {
          if (err?.status === 401) {
            this.error = 'Usuario o contraseña incorrectos';
          } else if (err?.name === 'TimeoutError') {
            this.error = 'Tiempo de espera agotado. Verifica el servidor.';
          } else {
            this.error = 'Error del servidor. Intenta nuevamente.';
          }
          this.cdr.detectChanges();
        },
      });
  }
}
