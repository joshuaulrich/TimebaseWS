import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  NgZone,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import {HdDate} from '@assets/hd-date/hd-date';
import {Store} from '@ngrx/store';
import {BsDatepickerConfig} from 'ngx-bootstrap/datepicker';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';
import {AppState} from '../../../core/store';
import {formatDate} from '../../../pages/streams/components/stream-details/stream-details.component';
import {getDateUsingTZ} from '../../locale.timezone';
import {TimeZone} from '../../models/timezone.model';
import {GlobalFiltersService} from '../../services/global-filters.service';
import {getTimeZones, getTimeZoneTitle} from '../../utils/timezone.utils';

@Component({
  selector: 'app-time-bar-picker',
  templateUrl: './time-bar-picker.component.html',
  styleUrls: ['./time-bar-picker.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TimeBarPickerComponent implements OnInit, OnDestroy {
  @Input() selectedDate: Date;
  @Output() selectedDateChange = new EventEmitter<Date>();
  @Input() startDate: Date;
  @Input() endDate: Date;
  @Input() method: string;
  public format: string;
  public visibleSelectedDate: Date;
  public startDate_title: string;
  public endDate_title: string;
  public selectorCursorTitle: string;
  @ViewChild('eventArea') eventArea: ElementRef;
  public cursorLeft = 0;
  public selectorCursorLeft = 0;
  public bsConfig: Partial<BsDatepickerConfig> = {
    containerClass: 'theme-default',
  };
  public dropdownListTimeZones: {
    nameTitle: string;
    name: string;
    offset: number;
  }[] = [];
  public dropdownSettingsTimeZone = {
    singleSelection: true,
    idField: 'name',
    textField: 'nameTitle',
    allowSearchFilter: true,
    closeDropDownOnSelection: true,
  };
  public selectedTimeZone = [];
  datePickerTimeZone: object;
  private destroy$ = new Subject();
  private hd_format: string;
  private avoidBSPickerTriggering = false;

  constructor(
    private appStore: Store<AppState>,
    public bsModalRef: BsModalRef,
    private cdr: ChangeDetectorRef,
    private _ngZone: NgZone,
    private globalFiltersService: GlobalFiltersService,
  ) {}

  ngOnInit() {
    this.dropdownListTimeZones = getTimeZones().map((item) => {
      return {nameTitle: this.getTimeZoneName(item), name: item.name, offset: item.offset};
    });
    if (!this.selectedDate) {
      this.selectedDate = new Date(this.endDate.toISOString());
      this.selectedDateChange.emit(this.selectedDate);
    }

    this.manualUpdateCursorPosition();

    this.globalFiltersService
      .getFilters()
      .pipe(takeUntil(this.destroy$))
      .subscribe((filters) => {
        const filter_date_format = filters.dateFormat[0];
        const filter_time_format = filters.timeFormat[0];
        this.selectedTimeZone = filters.timezone;
        this.datePickerTimeZone = this.getSelectedTZ();
        this.format = filter_date_format.toUpperCase() + ' ' + filter_time_format;
        this.hd_format = filter_date_format + ' ' + filter_time_format;

        this.format = this.format.replace('tt', 'A');
        this.format = this.format.replace(/f/g, 'S');

        this.startDate_title = formatDate(
          this.getDateUsingSelectedTZ(this.startDate).toISOString(),
          this.hd_format,
        );
        this.endDate_title = formatDate(
          this.getDateUsingSelectedTZ(this.endDate).toISOString(),
          this.hd_format,
        );

        setTimeout(() => {
          if (this.selectedTimeZone) {
            this.visibleSelectedDate = this.getDateUsingSelectedTZ(this.selectedDate);
          }
          this.cdr.detectChanges();
          this.cdr.markForCheck();
        }, 0);
      });
  }

  public onValueChange(newDate: Date) {
    if (this.avoidBSPickerTriggering) return;
    this.selectedDate = this.getDateWithoutSelectedTZ(newDate, this.getSelectedTZOffset());
    this.selectedDateChange.emit(this.selectedDate);
    this.manualUpdateCursorPosition();
  }

  public onTimeZoneSelected() {
    if (this.selectedTimeZone) {
      this.visibleSelectedDate = this.getDateUsingSelectedTZ(this.selectedDate);
      this.startDate_title = formatDate(
        this.getDateUsingSelectedTZ(this.startDate).toISOString(),
        this.hd_format,
      );
      this.endDate_title = formatDate(
        this.getDateUsingSelectedTZ(this.endDate).toISOString(),
        this.hd_format,
      );
      this.datePickerTimeZone = this.getSelectedTZ();
    }
  }

  public onTimeZoneDeSelected() {
    const tzName = Intl.DateTimeFormat().resolvedOptions().timeZone;
    this.selectedTimeZone = [
      this.dropdownListTimeZones.find((timezone) => timezone.name === tzName),
    ];
    this.onTimeZoneSelected();
  }

  public onSetDate(event: MouseEvent, percent: number = null) {
    event.stopImmediatePropagation();
    this.cursorLeft = percent !== null ? percent : this.cursorPosition(event.offsetX);
    this.selectedDate = this.getDate(this.cursorLeft);
    this.selectedDateChange.emit(this.selectedDate);
    this.visibleSelectedDate = this.getDateUsingSelectedTZ(this.selectedDate);
  }

  public onMouseMove(event: MouseEvent, fixedPercent = null) {
    this.selectorCursorLeft =
      fixedPercent !== null ? fixedPercent : this.cursorPosition(event.offsetX);
    const date = this.getDate(this.selectorCursorLeft);
    this.selectorCursorTitle = formatDate(
      this.getDateUsingSelectedTZ(date).toISOString(),
      this.hd_format,
    );
  }

  public ngOnDestroy(): void {
    this.destroy$.next(true);
    this.destroy$.complete();
  }

  private getTimeZoneName(item: TimeZone) {
    return getTimeZoneTitle(item);
  }

  private getSelectedTZOffset(): number {
    return this.getSelectedTZ().offset;
  }

  private getSelectedTZ() {
    return this.dropdownListTimeZones.find(
      (timezone) => timezone.name === this.selectedTimeZone[0].name,
    );
  }

  private getDateUsingSelectedTZ(date: Date) {
    return getDateUsingTZ(date, this.getSelectedTZ());
  }

  private getDateWithoutSelectedTZ(date: Date, offset: number): Date {
    const newDate = new Date(date.getTime()),
      localOffset = -new HdDate().getTimezoneOffset();
    newDate.setMilliseconds(newDate.getMilliseconds() - (offset - localOffset) * 60 * 1000);
    return newDate;
  }

  private manualUpdateCursorPosition() {
    const time = this.selectedDate.getTime() - this.startDate.getTime();
    this.cursorLeft = this.fitPercentage(
      this.getTimeRange() > 0 ? (time / this.getTimeRange()) * 100 : 0,
    );
  }

  private getDate(cursorLeft: number): Date {
    return new Date(this.startDate.getTime() + this.getTimeRange() * (cursorLeft / 100));
  }

  private cursorPosition(offsetX: number): number {
    return this.fitPercentage((offsetX / this.eventArea.nativeElement.offsetWidth) * 100);
  }

  private fitPercentage(percent: number): number {
    return Math.max(0, Math.min(100, percent));
  }

  private getTimeRange(): number {
    return this.endDate.getTime() - this.startDate.getTime();
  }
}
