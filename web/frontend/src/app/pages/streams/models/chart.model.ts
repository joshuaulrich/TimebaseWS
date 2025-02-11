export interface ChartModel {
  effectiveWindow: {
    end: string;
    start: string;
  };
  lines: ChartRowLines;
  name: string;
}

export interface ChartRowLines {
  [key: string]: ChartRawLine;
}

export interface ChartRawLine {
  aggregationSizeMs: number;
  newWindowSizeMs: number;
  points: ChartPointModel[];
}

export interface ChartPointModel {
  time: number;
  value: number | string;
  high?: number | string;
  low?: number | string;
  close?: number | string;
  open?: number | string;
  width?: number;
  askPrice?: number | string;
  bidPrice?: number | string;
}

export type ChartData = [
  {
    end: string;
    start: string;
  },
  any[],
  number,
];

export enum ChartTypes {
  BARS = 'BARS',
  TRADES_BBO = 'TRADES_BBO',
  PRICES_L2 = 'PRICES_L2',
}
