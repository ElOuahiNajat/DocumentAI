import { Component } from "@angular/core"
import { CommonModule } from "@angular/common"
import { FormsModule } from "@angular/forms"
import { RouterLink, RouterLinkActive } from "@angular/router"
import { MatToolbarModule } from "@angular/material/toolbar"
import { MatButtonModule } from "@angular/material/button"
import { MatIconModule } from "@angular/material/icon"
import { MatInputModule } from "@angular/material/input"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatSelectModule } from "@angular/material/select"
import { MatDividerModule } from "@angular/material/divider"
import { MatMenuModule } from "@angular/material/menu"

@Component({
  selector: "layout-header",
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    RouterLinkActive,
    MatToolbarModule, // Already imported
    MatButtonModule, // Already imported
    MatIconModule, // Already imported
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
  searchTerm = ""
  isProfileMenuOpen = false // State for the profile dropdown

  toggleProfileMenu() {
    this.isProfileMenuOpen = !this.isProfileMenuOpen
  }

  getTranslation(key: string): string {
    // This is a placeholder. You should replace this with your actual internationalization service.
    // For now, it will just return the key or a simple string.
    const translations: { [key: string]: string } = {
      home: "Home",
      documents: "Documents",
      users: "Users",
      profile: "Profile",
      settings: "Settings",
      logout: "Logout",
      // Add more translations as needed
    }
    return translations[key] || key // Return the translated string or the key itself if not found
  }
}
