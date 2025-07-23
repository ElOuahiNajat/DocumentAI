import { Component } from '@angular/core';
import {DocumentListComponent} from '../../components/document-list/document-list';

@Component({
  selector: 'app-document-page',
  imports: [
    DocumentListComponent,

  ],
  templateUrl: './document-page.html',
  styleUrl: './document-page.css'
})
export class DocumentPage {

}
