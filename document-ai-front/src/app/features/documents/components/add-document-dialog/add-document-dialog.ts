import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-add-document-dialog',
  standalone: true,
  templateUrl: './add-document-dialog.html',
  styleUrls: ['./add-document-dialog.css'],
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    CommonModule
  ]
})
export class AddDocumentDialog {
  documentForm: FormGroup;
  selectedFileName: string = '';

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddDocumentDialog>
  ) {
    this.documentForm = this.fb.group({
      title: ['',Validators.required],
      author: ['',Validators.required],
      description: ['',Validators.required],
      file: [null,Validators.required]
    });
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFileName = file.name; 
      this.documentForm.patchValue({ file });
      this.documentForm.get('file')?.markAsTouched();
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSubmit() {
    if (this.documentForm.invalid) {
      this.documentForm.markAllAsTouched(); 
      return;
    }

    const formData = new FormData();
    formData.append('title', this.documentForm.value.title);
    formData.append('author', this.documentForm.value.author);
    formData.append('description', this.documentForm.value.description);
    formData.append('file', this.documentForm.value.file);

    this.dialogRef.close(formData);
  }
}