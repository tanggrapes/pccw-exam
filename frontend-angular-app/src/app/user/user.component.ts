import { AddEditUserComponent } from './add-edit-user/add-edit-user.component';
import { UserService } from './user.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { SelectionModel } from '@angular/cdk/collections';
export interface User {
  id?: string;
  firstName: string;
  middleName: string;
  lastName: string;
  email: string;
  creationDate: Date;
}

export interface UsersResponse {
  content: User[];
  pageNo: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  dataSource: MatTableDataSource<User>;
  pageSizeOptions: number[] = [5, 10, 25, 100];
  userResponse: UsersResponse = {
    content: [],
    pageNo: 0,
    pageSize: 5,
    totalElements: 0,
    totalPages: 0,
    last: false,
  };
  selection = new SelectionModel<User>(true, []);

  displayedColumns: string[] = [
    'checkbox',
    'firstName',
    'middleName',
    'lastName',
    'email',
    'creationDate',
    'actions',
  ];
  constructor(private userService: UserService, private dialog: MatDialog) {}

  getUsers() {
    this.userService
      .getUsers(this.userResponse.pageNo, this.userResponse.pageSize)
      .subscribe((res: UsersResponse) => {
        this.dataSource = new MatTableDataSource(res.content);
        this.userResponse = res;
        this.selection = new SelectionModel<User>(true, []);
      });
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  masterToggle() {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.data.forEach((row) => this.selection.select(row));
  }

  deleteUser(id: string) {
    this.userService.delete(id).subscribe((res) => {
      alert('User Deleted!');
      this.getUsers();
    });
  }

  ngOnInit(): void {
    this.getUsers();
  }

  pageChanged(event: PageEvent) {
    this.userResponse.pageSize = event.pageSize;
    this.userResponse.pageNo = event.pageIndex;
    this.getUsers();
  }

  openAddEditUser(user = null) {
    const ref = this.dialog.open(AddEditUserComponent, {
      minHeight: '400px',
      minWidth: '600px',
      hasBackdrop: false,
      data: user,
    });
    ref.afterClosed().subscribe((isDataUpdated) => {
      if (isDataUpdated) {
        this.getUsers();
      }
    });
  }

  deleteAllSelected() {
    if (this.selection.isEmpty()) {
      alert('No User Selected');
      return;
    }
    const ids = this.selection.selected.map((user) => user.id).join(',');
    this.userService.deleteUsers(ids).subscribe((res) => {
      this.getUsers();
    });
  }
}
