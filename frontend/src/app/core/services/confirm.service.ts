import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ConfirmService {
  visible = signal(false);
  message = signal('');
  private resolveFn: ((result: boolean) => void) | null = null;

  confirm(message: string): Promise<boolean> {
    this.message.set(message);
    this.visible.set(true);
    return new Promise<boolean>(resolve => {
      this.resolveFn = resolve;
    });
  }

  respond(result: boolean): void {
    this.visible.set(false);
    this.resolveFn?.(result);
    this.resolveFn = null;
  }
}