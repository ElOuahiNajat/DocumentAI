import { FeedbackResponse } from './FeedbackResponse';

export interface DocumentResponse {
  id: string
  title: string
  author: string
  description: string
  createdAt: string
  updatedAt: string
  fileType: string
  fileSize: number
  feedbacks?: FeedbackResponse[];
  averageRating: number;
}
