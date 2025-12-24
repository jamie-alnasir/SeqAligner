# SeqAligner

A lightweight Java tool for performing **pairwise sequence alignment** using the classical **Needleman-Wunsch** (global) and **Smith-Waterman** (local) algorithms.

Designed for simple command-line use and educational or research purposes.

---

## About This Project

This code was originally developed by **Dr Jamie J. Alnasir** at the start of his **Ph.D. in Computer Science** (**with a focus on Distributed Computing for Large Datasets in Computational and Molecular Biology**) at **Royal Holloway, University of London**.

It forms part of early explorations into classical alignment algorithms and foundational bioinformatics methods, implemented for research and educational use.

---

## Features

- Global alignment (Needleman-Wunsch)
- Local alignment (Smith-Waterman)
- FASTA input support
- Match/mismatch scoring matrices
- Traceback matrix visualisation
- Multi-alignment output (alternative optimal paths)

---

## Requirements

- Java Runtime Environment (JRE)

---

## Compile

```bash
javac *.java
```

## Running

Method 1. With Two Seqence FASTA files

```bash
java SeqAligner seq1.fasta seq2.fasta
```

Method 2. Without any parameters, i.e. No FASTA files (will use internal sequences, as follows):

Sequence 1 = "ABCNYRCKLCRPMNP";
Sequence 2 = "AYCYNRCCRBPM";


```bash
java SeqAligner
```


