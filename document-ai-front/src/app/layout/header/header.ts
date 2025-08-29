import { Component, inject } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormsModule } from "@angular/forms"
import { RouterLink, RouterLinkActive, Router } from "@angular/router"
import { MatToolbarModule } from "@angular/material/toolbar"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatInputModule } from "@angular/material/input"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatSelectModule } from "@angular/material/select"
import { MatDividerModule } from "@angular/material/divider"
import { MatMenuModule } from "@angular/material/menu"
import { AuthService } from "../../features/auth/services/auth.service"

@Component({
  selector: "layout-header",
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDividerModule,
    MatMenuModule,
  ],
  templateUrl: "./header.html",
  styleUrls: ["./header.css"],
})
export class HeaderComponent {
  private authService = inject(AuthService)
  private router = inject(Router)

  searchTerm = ""
  isProfileMenuOpen = false

  toggleProfileMenu() {
    this.isProfileMenuOpen = !this.isProfileMenuOpen
  }

  onLogout() {
    // Close the profile menu first
    this.isProfileMenuOpen = false

    // Remove the access token and redirect to login
    this.authService.logout()
    this.router.navigate(["/login"])

    console.log("User logged out successfully")
  }

  // ✅ Enhanced method to check if user is admin
  isAdmin(): boolean {
    try {
      const user = this.authService.getCurrentUser()
      return user && user.role === "ADMIN"
    } catch (error) {
      console.error("Error checking admin status:", error)
      return false
    }
  }

  // ✅ Add method to check if user is authenticated
  isAuthenticated(): boolean {
    return this.authService.isAuthenticated()
  }

  // ✅ Add method to get current user info
  getCurrentUser(): any {
    return this.authService.getCurrentUser()
  }

  getTranslation(key: string): string {
    const translations: { [key: string]: string } = {
      home: "Home",
      documents: "Documents",
      users: "Users",
      profile: "Profile",
      settings: "Settings",
      logout: "Logout",
    }
    return translations[key] || key
  }
}
