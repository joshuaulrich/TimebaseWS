import { Big } from 'big.js';
export declare const ONE: Big;
export declare const ZERO: Big;
export declare const HUNDRED: Big;
export declare const THOUSAND: Big;
export declare const ONE_TENTH: Big;
export declare const ABBREVIATION_SUFFIXES: string[];
export declare const NAN: Big;
export declare const NULL: Big;
export declare const POSITIVE_INFINITY: Big;
export declare const NEGATIVE_INFINITY: Big;
export declare const negate: (value: Big) => Big;
export declare const decimalIsFinite: (value: Big) => boolean;
export declare const toDecimal: (decimal: string | number | Big) => Big;
