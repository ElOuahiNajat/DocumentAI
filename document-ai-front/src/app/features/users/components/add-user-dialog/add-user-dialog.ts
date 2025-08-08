import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { UserService } from '../../services/user-service';
import { CreateUserRequest } from '../../models/CreateUserRequest';
import { Role } from '../../models/role.enum';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-user-dialog',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDialogModule,
  ],
  templateUrl: './add-user-dialog.html',
})
export class AddUserDialog {
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  role: Role = Role.USER;

  roles = Object.values(Role);

  constructor(
    private userService: UserService,
    private dialogRef: MatDialogRef<AddUserDialog>,
    private snackBar: MatSnackBar
  ) {}

  onSubmit() {
    const newUser: CreateUserRequest = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
      role: this.role,
    };

    this.userService.createUser(newUser).subscribe({
      next: () => {
        this.snackBar.open('User added successfully!', 'Close', { duration: 3000 });
        this.dialogRef.close('success');
      },
      error: (error) => {
        this.snackBar.open(`Failed to add user: ${error.message}`, 'Close', { duration: 5000 });
      },
    });
  }

  onCancel() {
    this.dialogRef.close();
  }
}
