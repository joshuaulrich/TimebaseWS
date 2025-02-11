import {Injectable} from '@angular/core';
import {ContextMenuService} from 'ngx-contextmenu';
import {IContextMenuClickEvent} from 'ngx-contextmenu/lib/contextMenu.service';
import {take} from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ContextMenuControlService {
  private openMenu: string;

  constructor(private context: ContextMenuService) {}

  show(event: IContextMenuClickEvent, alias = null) {
    this.context.show.next(event);
    this.context.close.pipe(take(1)).subscribe(() => {
      if (this.openMenu === alias) {
        this.openMenu = null;
      }
    });
    this.openMenu = alias;
    const mouseEvent = event.event;
    mouseEvent.stopPropagation();
    mouseEvent.stopImmediatePropagation();
    mouseEvent.preventDefault();
  }

  closeMenu(alias: string) {
    if (this.openMenu === alias) {
      this.context.closeAllContextMenus(null);
    }
  }
}
