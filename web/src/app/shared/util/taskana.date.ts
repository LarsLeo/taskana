import {DatePipe} from '@angular/common';

export class TaskanaDate {
  public static getCurrentDateString(format: string): string {
    const dateFormat = format === 'ISO' ? 'yyyy-MM-ddTHH:mm:ss.sss' : 'yyyy-MM-dd';
    const dateLocale = 'en-US';
    const datePipe = new DatePipe(dateLocale);
    return datePipe.transform(Date.now(), dateFormat) + (format === 'ISO' ? 'Z' : '');
  }

  public static getDateString(date: Date, format: string): string {
    const dateFormat = format === 'ISO' ? 'yyyy-MM-ddTHH:mm:ss.sss' : 'yyyy-MM-dd';
    const dateLocale = 'en-US';
    const datePipe = new DatePipe(dateLocale);
    return datePipe.transform(date, dateFormat) + (format === 'ISO' ? 'Z' : '');
  }

  public static getDate(date: string): Date {
    if (date.indexOf(':') !== -1) {
      date = date.substring(0, date.indexOf(':'));
    }
    let bits = date.split('-').map(element => parseInt(element, 10));
    return new Date(bits[0], bits[1], bits[2]);
  }

  // public static getDate(): string {
  //   const dateFormat = 'yyyy-MM-ddTHH:mm:ss.sss';
  //   const dateLocale = 'en-US';
  //   const datePipe = new DatePipe(dateLocale);
  //   return datePipe.transform(Date.now(), dateFormat) + 'Z';
  // }

  // public static convertSimpleDate(date: Date): string {
  //   const dateFormat = 'yyyy-MM-dd';
  //   const dateLocale = 'en-US';
  //   const datePipe = new DatePipe(dateLocale);
  //   return datePipe.transform(date, dateFormat);
  // }

  // public static getISODate(date: Date): string {
  //   const dateFormat = 'yyyy-MM-ddTHH:mm:ss.sss';
  //   const dateLocale = 'en-US';
  //   const datePipe = new DatePipe(dateLocale);
  //   return datePipe.transform(date, dateFormat) + 'Z';
  // }
}
