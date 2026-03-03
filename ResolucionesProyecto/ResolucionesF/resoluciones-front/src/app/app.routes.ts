import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { CompleteProfileComponent } from './auth/complete-profile/complete-profile.component';
import { LayoutComponent } from './layout/layout.component';
import { PrincipalComponent } from './pages/principal/principal.component';
import { SessionsComponent } from './pages/sessions/sessions.component';
import { ActsComponent } from './pages/acts/acts.component';
import { RolComponent } from './pages/rol/rol';
import { UserRolesComponent } from './pages/user-roles/user-roles';
import { PermissionComponent } from './pages/permission/permission';
import { RolPermissionsComponent } from './pages/rol-permissions/rol-permissions';
import { UserComponent } from './pages/user/user';
import { InstanceComponent } from './pages/instance/instance.component';
import { ResolutionsComponent } from './pages/resolutions/resolutions';
import { ResolutionDetailComponent } from './pages/resolution-detail/resolution-detail.component';
import { HierarchicalLevelsComponent } from './pages/hierarchical-levels/hierarchical-levels.component';
import { BinnacleComponent } from './pages/binnacle/binnacle.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';
import { FacultyComponent } from './pages/faculty/faculty.component';
import { MajorsComponent } from './pages/majors/majors.component';
import { SelectInstanceComponent } from './auth/select-instance/select-instance.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegisterComponent },
  { path: 'completar-perfil', component: CompleteProfileComponent, canActivate: [authGuard] },
  { path: 'seleccionar-instancia', component: SelectInstanceComponent },

  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'inicio', component: PrincipalComponent },
      { path: 'resoluciones', component: ResolutionsComponent, data: { permissions: ['RESOLUTION_VIEW'] } },
      { path: 'resoluciones/:id', component: ResolutionDetailComponent, data: { permissions: ['RESOLUTION_VIEW'] } },
      { path: 'sessions', component: SessionsComponent, data: { permissions: ['SESSION_VIEW'] } },
      { path: 'sessions/:id/acts', component: ActsComponent, data: { permissions: ['ACT_VIEW'] } },
      { path: 'instances', component: InstanceComponent, data: { permissions: ['INSTANCE_MANAGE'] } },
      { path: 'user', component: UserComponent, data: { permissions: ['USER_MANAGE'] } },
      { path: 'roles', component: RolComponent, data: { permissions: ['ROLE_MANAGE'] } },
      { path: 'roles/:id/permissions', component: RolPermissionsComponent, data: { permissions: ['ROLE_MANAGE'] } },
      { path: 'permisos', component: PermissionComponent, data: { permissions: ['PERMISSION_MANAGE'] } },
      { path: 'user-roles', component: UserRolesComponent, data: { permissions: ['USER_ROLE_MANAGE'] } },
      { path: 'niveles-jerarquicos', component: HierarchicalLevelsComponent, data: { permissions: ['LEVEL_MANAGE'] } },
      { path: 'bitacora', component: BinnacleComponent, data: { permissions: ['BINNACLE_VIEW'] } },
      { path: 'notificaciones', component: NotificationsComponent },
      { path: 'facultades', component: FacultyComponent, data: { permissions: ['FACULTY_MANAGE'] } },
      { path: 'carreras', component: MajorsComponent, data: { permissions: ['MAJOR_MANAGE'] } },
      { path: '', redirectTo: 'inicio', pathMatch: 'full' },
    ],
  },

  { path: '**', redirectTo: '/login' },
];
