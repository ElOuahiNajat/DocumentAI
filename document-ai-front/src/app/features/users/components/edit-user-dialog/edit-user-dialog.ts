import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { UserService } from '../../services/user-service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-user-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule],
  template: `
    <div class="dialog-container">
      <h2 class="dialog-title">Edit User</h2>
      <form (ngSubmit)="updateUser()" class="form">
        <mat-form-field appearance="fill" class="full-width">
          <mat-label>First Name</mat-label>
          <input matInput [(ngModel)]="data.firstName" name="firstName" required />
        </mat-form-field>

        <mat-form-field appearance="fill" class="full-width">
          <mat-label>Last Name</mat-label>
          <input matInput [(ngModel)]="data.lastName" name="lastName" required />
        </mat-form-field>

        <mat-form-field appearance="fill" class="full-width">
          <mat-label>Email</mat-label>
          <input matInput type="email" [(ngModel)]="data.email" name="email" required />
        </mat-form-field>

        <mat-form-field appearance="fill" class="full-width">
          <mat-label>Role</mat-label>
          <mat-select [(ngModel)]="data.role" name="role" required>
            <mat-option value="ADMIN">ADMIN</mat-option>
            <mat-option value="USER">USER</mat-option>
          </mat-select>
        </mat-form-field>

        <div class="button-row">
          <button mat-button type="button" (click)="dialogRef.close()">Cancel</button>
          <button mat-raised-button color="primary" type="submit">Update</button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    :host {
      box-sizing: border-box;
      font-family: 'Roboto', sans-serif;
    }

    .dialog-container {
      width: 400px;
      padding: 24px;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 4px 16px rgba(0,0,0,0.12);
    }

    .dialog-title {
      margin: 0 0 20px 0;
      font-size: 22px;
      font-weight: 600;
      text-align: center;
      color: #1976d2;
    }

    .form {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .full-width {
      width: 100%;
    }

    .button-row {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
      margin-top: 24px;
    }

    button[mat-button] {
      color: #1976d2;
      font-weight: 500;
      transition: background-color 0.2s ease;
    }

    button[mat-button]:hover {
      background-color: rgba(25, 118, 210, 0.08);
    }

    button[mat-raised-button] {
      background-color: #1976d2;
      color: white;
      font-weight: 600;
      padding: 0 18px;
      transition: background-color 0.2s ease;
    }

    button[mat-raised-button]:hover {
      background-color: #115293;
    }

  `]
})
export class EditUserDialog {
  constructor(
    public dialogRef: MatDialogRef<EditUserDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private userService: UserService,
    private snackBar: MatSnackBar
  ) {}

  updateUser() {
    this.userService.updateUser(this.data.id, {
      firstName: this.data.firstName,
      lastName: this.data.lastName,
      email: this.data.email,
      role: this.data.role
    }).subscribe({
      next: () => {
        this.snackBar.open('User updated successfully!', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['snackbar-success']
        });
        this.dialogRef.close('success');
      },
      error: () => {
        this.snackBar.open('Failed to update user', 'Close', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['snackbar-error']
        });
      }
    });
  }
}

