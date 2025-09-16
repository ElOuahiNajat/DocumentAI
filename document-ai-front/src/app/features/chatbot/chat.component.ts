import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

interface Message {
  sender: 'user' | 'bot';
  text: string;
}

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  chatForm: FormGroup;
  messages: Message[] = [];
  loading = false;

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.chatForm = this.fb.group({
      question: ['', Validators.required]
    });
  }

  sendMessage() {
    if (this.chatForm.invalid) return;

    const question = this.chatForm.value.question;
    this.messages.push({ sender: 'user', text: question });
    this.loading = true;

    this.http.post<any>('http://127.0.0.1:5000/chat', { question })
      .subscribe({
        next: (res) => {
          this.messages.push({ sender: 'bot', text: res.response });
          this.chatForm.reset();
          this.loading = false;
        },
        error: (err) => {
          this.messages.push({ sender: 'bot', text: 'Erreur serveur' });
          this.loading = false;
        }
      });
  }
}
