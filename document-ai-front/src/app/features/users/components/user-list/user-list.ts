import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatPaginator } from '@angular/material/paginator';
import { UserService } from '../../services/user-service';
import { UserResponse } from '../../models/user-response';
import { PagedResponse } from '../../models/paged-response';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MatPaginatorModule],
  templateUrl: './user-list.html',
  styleUrls: ['./user-list.css']
})
export class UserList implements OnInit {
  users: UserResponse[] = [];
  currentPage: number = 0;
  pageSize: number = 10;
  totalItems: number = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private userService: UserService) {}

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
}
