export class Alert {
    id!: string;
    type!: AlertType;
    message: string | undefined;
    autoClose: boolean | undefined;
    keepAfterRouteChange: boolean | undefined;
    fade: boolean | undefined;

    constructor(init?:Partial<Alert>) {
        Object.assign(this, init);
    }
}

export enum AlertType {
    Success,
    Error,
    Info,
    Warning
}