import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatPaginator } from '@angular/material/paginator';
import { UserService } from '../../services/user-service';
import { UserResponse } from '../../models/user-response';
import { PagedResponse } from '../../models/paged-response';
import { MatDialog } from '@angular/material/dialog';
import { AddUserDialog } from '../add-user-dialog/add-user-dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatPaginatorModule, MatButtonModule, MatDialogModule],
  templateUrl: './user-list.html',
  styleUrls: ['./user-list.css']
})
export class UserList implements OnInit {
  users: UserResponse[] = [];
  currentPage: number = 0;
  pageSize: number = 10;
  totalItems: number = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private userService: UserService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(): void {
    this.userService.getUsers(this.currentPage, this.pageSize).subscribe({
      next: (res: PagedResponse<UserResponse>) => {
        this.users = res.content;
        this.totalItems = res.totalElements;
      },
      error: (err) => {
        console.error('Error loading users', err);
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.currentPage = event.pageIndex;
    this.getUsers();
  }

  onEdit(user: UserResponse): void {
    console.log('Modifier utilisateur:', user);
  }

  onDelete(user: UserResponse): void {
    console.log('Supprimer utilisateur:', user);
  }

  openAddUserDialog(): void {
  const dialogRef = this.dialog.open(AddUserDialog);

  dialogRef.afterClosed().subscribe(result => {
    if (result === 'success') {
      this.getUsers();
    }
  });
}

}
