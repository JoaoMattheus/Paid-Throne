</main>
<footer class="site-footer">
    <div class="container footer__content">
        <div>
            <span>&copy; <?= date('Y'); ?> <?= sanitize_output(env('APP_NAME', 'Trono Remunerado')); ?>.</span>
            <span>Todos os direitos reservados.</span>
        </div>
        <nav aria-label="Rodapé">
            <a href="/privacidade.php">Privacidade</a>
            <a href="/contato.php">Contato</a>
        </nav>
    </div>
</footer>
<script src="<?= asset('assets/js/main.js'); ?>" defer></script>
</body>
</html>
