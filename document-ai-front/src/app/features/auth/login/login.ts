import { Component, type OnInit, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormBuilder, type FormGroup, Validators, ReactiveFormsModule } from "@angular/forms"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatCardModule } from "@angular/material/card"
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner"
import { Router } from "@angular/router"
import {AuthService} from '../services/auth.service';
import type {AuthenticationRequest} from '../models/AuthenticationRequest';

@Component({
  selector: "app-login",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: "./login.html",
  styleUrls: ["./login.css"],
})
export class LoginComponent implements OnInit {
  private formBuilder = inject(FormBuilder)
  private authService = inject(AuthService)
  private router = inject(Router)

  loginForm!: FormGroup
  isLoading = false
  errorMessage: string | null = null
  hidePassword = true

  ngOnInit(): void {
    this.initializeForm()

    // Redirect if already authenticated
    if (this.authService.isAuthenticated()) {
      this.router.navigate(["/documents"])
    }
  }

  private initializeForm(): void {
    this.loginForm = this.formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, ]],
    })
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true
      this.errorMessage = null

      const authRequest: AuthenticationRequest = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password,
      }

      this.authService.login(authRequest).subscribe({
        next: (response) => {
          console.log("Login successful:", response)
          this.authService.setToken(response.accessToken)
          this.router.navigate(["/documents"])
        },
        error: (error) => {
          console.error("Login failed:", error)

          if (error.status === 401) {
            this.errorMessage = "Invalid email or password"
          } else if (error.status === 0) {
            this.errorMessage = "Unable to connect to server"
          } else {
            this.errorMessage = "Login failed. Please try again."
          }

          this.isLoading = false
        },
        complete: () => {
          this.isLoading = false
        },
      })
    } else {
      this.markFormGroupTouched()
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach((key) => {
      const control = this.loginForm.get(key)
      control?.markAsTouched()
    })
  }

  getEmailErrorMessage(): string {
    const emailControl = this.loginForm.get("email")
    if (emailControl?.hasError("required")) {
      return "Email is required"
    }
    if (emailControl?.hasError("email")) {
      return "Please enter a valid email address"
    }
    return ""
  }

  getPasswordErrorMessage(): string {
    const passwordControl = this.loginForm.get("password")
    if (passwordControl?.hasError("required")) {
      return "Password is required"
    }
    if (passwordControl?.hasError("minlength")) {
      return "Password must be at least 6 characters long"
    }
    return ""
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword
  }
}
