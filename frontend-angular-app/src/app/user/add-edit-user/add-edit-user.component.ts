import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from '../user.component';
import { UserService } from '../user.service';
@Component({
  selector: 'app-add-edit-user',
  templateUrl: './add-edit-user.component.html',
  styleUrls: ['./add-edit-user.component.scss'],
})
export class AddEditUserComponent implements OnInit {
  isLoading = false;
  userFormGroup: FormGroup;
  user: User;
  title = 'Register User';
  welcomeEmail: string;
  constructor(
    public dialogRef: MatDialogRef<AddEditUserComponent>,
    private formBuilder: FormBuilder,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) data
  ) {
    if (data) {
      this.user = data;
      this.title = 'Edit User';
      this.userService.getWelcomeEmail(this.user.email).subscribe((res) => {
         this.welcomeEmail = res[0].textAsHtml
      });
    }
    this.userFormGroup = this.formBuilder.group({
      firstName: [this.user?.firstName, [Validators.required]],
      lastName: [this.user?.lastName, [Validators.required]],
      middleName: [this.user?.middleName],
      email: [this.user?.email, [Validators.required, Validators.email]],
    });
  }

  private handleError(err) {
    if (err.error.message) {
      alert(err.error.message);
      return;
    }

    if (err.error.errors) {
      alert(err.error.errors.join('\n'));
      return;
    }
  }

  saveUser() {
    this.isLoading = true;
    if (this.user) {
      this.userService
        .editUser({ ...this.userFormGroup.value, id: this.user.id })
        .subscribe(
          (res) => {
            alert('User saved!');
            this.isLoading = false;
            this.dialogRef.close(true);
          },
          (err) => {
            console.log(err);
            this.isLoading = false;
            this.handleError(err);
          }
        );
    } else {
      this.userService.registerUser(this.userFormGroup.value).subscribe(
        (res) => {
          alert('User saved!');
          this.isLoading = false;
          this.dialogRef.close(true);
        },
        (err) => {
          console.log(err);
          this.isLoading = false;
          this.handleError(err);
        }
      );
    }
  }

  ngOnInit(): void {}
}
