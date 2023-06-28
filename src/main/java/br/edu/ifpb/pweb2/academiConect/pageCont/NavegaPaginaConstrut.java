package br.edu.ifpb.pweb2.academiConect.pageCont;

public class NavegaPaginaConstrut {

    private NavegaPagina paginator;

    public static NavegaPagina newNavPage(int currentPage, long totalItems, int totalPages, int pageSize) {
        NavegaPaginaConstrut builder = new NavegaPaginaConstrut();
        builder.start();
        builder.setCurrentPage(currentPage);
        builder.setTotalItems(totalItems);
        builder.setTotalPages(totalPages);
        builder.setPageSize(pageSize);
        return builder.finish();
    }

    private NavegaPaginaConstrut() {
        this.start();
    }

    public NavegaPaginaConstrut start() {
        this.paginator = new NavegaPagina();
        return this;
    }

    public NavegaPaginaConstrut setCurrentPage(int currentPage) {
        this.paginator.setCurrentPage(currentPage);
        return this;
    }

    public NavegaPaginaConstrut setTotalItems(long totalItems) {
        this.paginator.setTotalItems(totalItems);
        return this;
    }

    public NavegaPaginaConstrut setTotalPages(int totalPages) {
        this.paginator.setTotalPages(totalPages);
        return this;
    }

    public NavegaPaginaConstrut setPageSize(int pageSize) {
        this.paginator.setPageSize(pageSize);
        return this;
    }

    public NavegaPagina finish() {
        return this.paginator;
    }

    
}
