import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { finalize, timeout } from 'rxjs/operators';

@Component({
  selector: 'app-complete-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './complete-profile.component.html',
  styleUrls: ['./complete-profile.component.css'],
})
export class CompleteProfileComponent {
  names = '';
  surnames = '';
  phone = '';
  newPassword = '';
  confirmPassword = '';
  showPassword = false;
  showConfirmPassword = false;

  loading = false;
  error: string | null = null;

  constructor(
    private http: HttpClient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  get formValid(): boolean {
    return (
      this.names.trim().length > 0 &&
      this.surnames.trim().length > 0 &&
      (this.newPassword === '' || this.newPassword.length >= 6) &&
      this.newPassword === this.confirmPassword
    );
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPassword(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  submit(): void {
    if (this.loading || !this.formValid) return;

    this.error = null;
    this.loading = true;
    this.cdr.detectChanges();

    const body: any = {
      names: this.names.trim(),
      surnames: this.surnames.trim(),
      phone: this.phone.trim() || null,
    };

    if (this.newPassword) {
      body.newPassword = this.newPassword;
    }

    this.http
      .put<{ message: string }>('/api/auth/complete-profile', body)
      .pipe(
        timeout(15000),
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: () => {
          // Update localStorage to mark profile as completed
          const raw = localStorage.getItem('user');
          if (raw) {
            const user = JSON.parse(raw);
            user.profileCompleted = true;
            user.names = body.names;
            user.surnames = body.surnames;
            localStorage.setItem('user', JSON.stringify(user));
          }
          this.router.navigate(['/inicio']);
        },
        error: (err) => {
          if (err?.status === 400) {
            this.error = err?.error?.message || 'Datos no validos. Verifica la informacion.';
          } else if (err?.name === 'TimeoutError') {
            this.error = 'Tiempo de espera agotado. Intenta nuevamente.';
          } else {
            this.error = 'Error del servidor. Intenta mas tarde.';
          }
          this.cdr.detectChanges();
        },
      });
  }
}
