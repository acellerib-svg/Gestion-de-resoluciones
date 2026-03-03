import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService, InstanceSimple } from '../auth.service';

@Component({
  selector: 'app-select-instance',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './select-instance.component.html',
  styleUrls: ['./select-instance.component.css'],
})
export class SelectInstanceComponent implements OnInit {
  instances: InstanceSimple[] = [];
  loading = false;
  error: string | null = null;

  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }
    const raw = localStorage.getItem('availableInstances');
    if (raw) {
      this.instances = JSON.parse(raw);
    }
    if (this.instances.length === 0) {
      this.router.navigate(['/inicio']);
    }
  }

  select(inst: InstanceSimple): void {
    if (this.loading) return;
    this.loading = true;
    this.error = null;
    const user = this.auth.getSession();
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }
    this.auth.loadMe(user.id, inst.id).subscribe({
      next: () => {
        localStorage.removeItem('availableInstances');
        this.router.navigate(['/inicio']);
      },
      error: () => {
        this.error = 'Error al cargar permisos. Intenta de nuevo.';
        this.loading = false;
      },
    });
  }
}
