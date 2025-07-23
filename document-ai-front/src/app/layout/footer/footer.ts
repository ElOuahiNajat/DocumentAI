import { Component } from "@angular/core"
import { CommonModule } from "@angular/common"
import {MatToolbar} from "@angular/material/toolbar";

@Component({
  selector: "layout-footer",
  standalone: true,
    imports: [CommonModule, MatToolbar],
  templateUrl: "./footer.html",
  styleUrls: ["./footer.css"], // You can add specific styles here if needed, but Tailwind should suffice
})
export class FooterComponent {
  currentYear: number = new Date().getFullYear()
}
