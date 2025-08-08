import { Component } from '@angular/core';
import {UserList} from '../../components/user-list/user-list';

@Component({
  selector: 'users-users-list',
  imports: [UserList],
  templateUrl: './users-list.html',
})
export class UsersList {

}
