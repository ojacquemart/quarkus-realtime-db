declare class ClipboardItem {
  constructor(data: { [mimeType: string]: Blob });
}

interface Clipboard {
  read(): Promise<DataTransfer>;

  write(data: ClipboardItem[]): Promise<void>;
}
