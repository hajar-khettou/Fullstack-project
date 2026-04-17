import { Injectable, signal } from '@angular/core';

export type Role = 'USER' | 'EDITOR' | 'WEBMASTER';

export interface AuthUser {
  username: string;
  password: string;
  role: Role;
}

const ROLE_MAP: Record<string, Role> = {
  user: 'USER',
  editor: 'EDITOR',
  admin: 'WEBMASTER',
};

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUser = signal<AuthUser | null>(null);

  login(username: string, password: string): boolean {
    const role = ROLE_MAP[username.toLowerCase()];
    if (!role) return false;
    this.currentUser.set({ username, password, role });
    return true;
  }

  logout(): void {
    this.currentUser.set(null);
  }

  getUser(): AuthUser | null {
    return this.currentUser();
  }

  isLoggedIn(): boolean {
    return this.currentUser() !== null;
  }

  hasRole(role: Role): boolean {
    return this.currentUser()?.role === role;
  }

  getBasicAuthHeader(): string | null {
    const user = this.currentUser();
    if (!user) return null;
    return 'Basic ' + btoa(`${user.username}:${user.password}`);
  }
}
