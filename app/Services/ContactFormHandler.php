<?php

declare(strict_types=1);

namespace App\Services;

final class ContactFormHandler
{
    private const LOG_FILE = BASE_PATH . '/storage/logs/contact.log';

    /**
     * @param array{nome:string,email:string,mensagem:string} $data
     * @return array{success:bool,message:string}
     */
    public function handle(array $data): array
    {
        $recipient = env('CONTACT_RECIPIENT_EMAIL');
        $subject = 'Novo contato via Trono Remunerado';
        $safeName = $this->sanitizeHeaderValue($data['nome']);
        $safeEmail = $this->sanitizeHeaderValue($data['email']);
        $safeMessage = $this->sanitizeBody($data['mensagem']);

        $body = sprintf("Nome: %s\nEmail: %s\nMensagem:\n%s\nData: %s\n",
            $safeName,
            $safeEmail,
            $safeMessage,
            (new \DateTimeImmutable())->format('d/m/Y H:i:s')
        );

        if ($recipient && filter_var($recipient, FILTER_VALIDATE_EMAIL)) {
            $headers = sprintf("From: %s\r\nReply-To: %s\r\n", $safeEmail, $safeEmail);
            $sent = @mail($recipient, $subject, $body, $headers);
            if ($sent) {
                return [
                    'success' => true,
                    'message' => 'Mensagem enviada com sucesso! Assim que possível respondemos ao seu chamado real.',
                ];
            }
        }

        $this->logToFile($body);

        return [
            'success' => true,
            'message' => 'Mensagem registrada! Caso o envio de e-mail não esteja configurado, sua mensagem foi salva com segurança.',
        ];
    }

    private function logToFile(string $content): void
    {
        $directory = dirname(self::LOG_FILE);
        if (!is_dir($directory)) {
            mkdir($directory, 0775, true);
        }

        file_put_contents(self::LOG_FILE, $content . str_repeat('-', 60) . PHP_EOL, FILE_APPEND | LOCK_EX);
    }

    private function sanitizeHeaderValue(string $value): string
    {
        $value = trim($value);
        $value = preg_replace('/[\r\n]+/', ' ', $value) ?? '';
        return substr($value, 0, 255);
    }

    private function sanitizeBody(string $value): string
    {
        $normalized = preg_replace("/[\r\n]+/", PHP_EOL, trim($value)) ?? '';
        return $normalized;
    }
}
