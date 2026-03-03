import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { finalize, timeout } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  email = '';

  loading = false;
  error: string | null = null;
  success: string | null = null;

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  get emailValid(): boolean {
    return /^[a-zA-Z0-9._%+-]+@uteq\.edu\.ec$/.test(this.email.trim());
  }

  register(): void {
    if (this.loading) return;

    this.error = null;
    this.success = null;

    const e = this.email.trim().toLowerCase();

    if (!e) {
      this.error = 'Ingresa tu correo institucional.';
      this.cdr.detectChanges();
      return;
    }

    if (!this.emailValid) {
      this.error = 'Solo se permiten correos @uteq.edu.ec';
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;
    this.cdr.detectChanges();

    this.http
      .post<{ message: string }>('/api/auth/register', { email: e })
      .pipe(
        timeout(15000),
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (resp) => {
          this.success = resp.message;
          this.cdr.detectChanges();
        },
        error: (err) => {
          if (err?.status === 400) {
            this.error =
              err?.error?.message || err?.error?.detail || 'Datos no válidos. Verifica tu correo.';
          } else if (err?.name === 'TimeoutError') {
            this.error = 'Tiempo de espera agotado. Intenta nuevamente.';
          } else {
            this.error = 'Error del servidor. Intenta más tarde.';
          }
          this.cdr.detectChanges();
        },
      });
  }
}
