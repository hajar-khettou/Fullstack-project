import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    if (!this.username || !this.password) {
      this.error = 'Veuillez remplir tous les champs.';
      return;
    }
    const ok = this.authService.login(this.username, this.password);
    if (ok) {
      this.router.navigate(['/']);
    } else {
      this.error = 'Identifiant inconnu. Utilisez : user, editor ou admin.';
    }
  }
}
